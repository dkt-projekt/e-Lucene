package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.filemanagement.FileFactory;
import de.dkt.common.niftools.NIFReader;
import de.dkt.common.niftools.NIFWriter;
import de.dkt.eservices.elucene.exceptions.LuceneIndexExistsException;
import de.dkt.eservices.elucene.exceptions.UnSupportedDocumentParserFormatException;
import de.dkt.eservices.elucene.indexmanagement.analyzer.AnalyzerFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.DocumentParserFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.IDocumentParser;
import de.dkt.eservices.elucene.indexmanagement.documentparser.NIFDocumentParser;
import de.dkt.eservices.elucene.indexmanagement.queryparser.OwnQueryParser;
import de.dkt.eservices.elucene.indexmanagement.resultconverter.LuceneResultConverterToJENA;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;
import eu.freme.common.persistence.dao.IndexDAO;
import eu.freme.common.persistence.model.LuceneIndex;
import eu.freme.common.persistence.repository.IndexRepository;

/**
 * Configures Lucene indexing files
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 *
 * NOTE: keep in mind the exception  LockObtainFailedException. that can appear when accesing concurrently to an index.
 */
public class LuceneModule {

	static Logger logger = Logger.getLogger(LuceneModule.class);
	
	private IndexRepository indexRepository;

	private IndexDAO indexDao;

	private static LuceneModule lucene;

	private boolean indexCreate = false;
	private String luceneIndexPath;

	protected LuceneModule (String repositoriesPath,IndexRepository indexRepository, IndexDAO indexDAO){
		if(repositoriesPath.endsWith(File.separator)){
			luceneIndexPath = repositoriesPath;
		}
		else{
			luceneIndexPath = repositoriesPath + File.separator;
		}
		this.indexRepository = indexRepository;
		this.indexDao=indexDAO;
	}
	
	public static LuceneModule getInstance(String repositoriesPath,IndexRepository indexRepository, IndexDAO indexDAO){
	     if(lucene == null) {
	         lucene = new LuceneModule(repositoriesPath,indexRepository,indexDAO);
	      }
	      return lucene;
	}
	
	public String listIndexes() {
		try {
			List<LuceneIndex> indexesList = indexRepository.findAll();
			JSONObject indexes = new JSONObject();
			int counter = 0;
			for (LuceneIndex i : indexesList) {
				indexes.put("index"+counter, i.getIndexId());
				counter++;
			}
			JSONObject obj = new JSONObject();
			obj.put("indexes", indexes);
			return obj.toString();
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	public String deleteIndex(String indexId) {
		if(!deleteIndexFiles(indexId)){
			String msg = "Error at deleting the files from the index "+indexId;
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}
		if(!deleteIndexDDBB(indexId)){
			String msg = "Error at deleting the ddbb entry of the index "+indexId;
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}
		return String.format("Index \"%s\" correctly deleted", indexId);
	}

	public boolean deleteIndexFiles(String indexId) {
		try {
			File f = FileFactory.generateFileInstance(luceneIndexPath+ indexId);
			boolean error = false;
			File[] files = f.listFiles();
			for (File f2 : files) {
				if(!f2.delete()){
					error=true;
				}
			}
			if(!f.delete()){
				error=true;
			}
			if(error){
				throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, "Some files have not been deleted. Check manually again.");
			}
			return true;
		}
		catch(IOException e){
			return false;
		}
	}

	public boolean deleteIndexDDBB(String indexId) {
		try{
//			System.out.println("DELETE: "+indexId);
			indexDao.deleteByIndexId(indexId);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/** 
	 * Create a new index.
	 * 
	 * @param indexId
	 * @param language
	 * @param sFields
	 * @param sAnalyzers
	 * @return
	 * @throws IOException
	 * @throws ExternalServiceFailedException
	 */
	public String createIndex(String indexId, String language, String sFields, String sAnalyzers, boolean overwrite) throws IOException,ExternalServiceFailedException{
		if(!createIndexFiles(indexId, language, sFields, sAnalyzers, overwrite)){
			String msg = String.format("Error at creating the files for index \"%s\"",indexId);
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}
		if(!createIndexDDBB(indexId,language,sFields,sAnalyzers)){
			String msg = String.format("Error at creating the DDBB entry for index \"%s\"",indexId);
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}
		return String.format("The index \"%s\" has been correctly generated", indexId);
	}

	public boolean createIndexFiles(String indexId, String language, String sFields, String sAnalyzers, boolean overwrite) throws IOException,ExternalServiceFailedException{
		//TODO Check if the index exists, then throw Exception.
//		System.out.println(luceneIndexPath + indexId);
		File f = FileFactory.generateOrCreateDirectoryInstance(luceneIndexPath + indexId);
		Path path = f.toPath();
		Directory dir = FSDirectory.open(path);

		IndexWriterConfig iwc = null;
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		String[] fields = sFields.split(";");
		String[] analyzers = sAnalyzers.split(";");
		if(fields.length!=analyzers.length){
			String msg = "The number of fields and analyzers is different";
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		
		for (int i = 0; i < fields.length; i++) {
//			System.out.println("GENERATING "+analyzers[i]+" for "+fields[i]);
			Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language);
			analyzerMap.put(fields[i], particularAnalyzer);
		}
		PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
				AnalyzerFactory.getAnalyzer("standard", language), 
				analyzerMap);
		iwc = new IndexWriterConfig(wrapper);

		if(overwrite){
			iwc.setOpenMode(OpenMode.CREATE);
		}
		else{
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		// Optional: for better indexing performance, if you are indexing many documents, increase the RAM
		// buffer.  But if you do this, increase the max heap size to the JVM (eg add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);
		IndexWriter writer = new IndexWriter(dir, iwc);
		writer.commit();
		writer.close();
		return true;
	}

	public boolean createIndexDDBB(String indexId, String language, String sFields, String sAnalyzers) throws ExternalServiceFailedException{
		try{
			if(indexRepository==null){
				System.out.println("++++++++++++++**************NNNNNNNUUUUUUUULLLLLLLLLLLLLLLLL");
			}
			LuceneIndex li = indexRepository.findOneByIndexId(indexId);
			if(li==null){
				LuceneIndex index = new LuceneIndex();
				index.setIndexId(indexId);
				index.setFields(sFields);
				index.setAnalyzers(sAnalyzers);
				index.setLanguage(language);
				index.setCreationTime(new Date());
				index = indexRepository.save(index);
				return true;
			}
			else{
				logger.error("There is an existing lucene_index with the same name");
				throw new LuceneIndexExistsException();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	public Model deleteAndRetrieveDocumentFromModel(Model docContent,String indexId) throws IOException,ExternalServiceFailedException{
		LuceneIndex index = indexRepository.findOneByIndexId(indexId);
		if(index==null){
			String msg = String.format("The index \"%s\" does not exist.",indexId);
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		String documentId = NIFReader.extractDocumentWholeURI(docContent);
		return deleteAndRetrieveDocument(documentId, indexId);
	}

	public Model deleteAndRetrieveDocument(String documentId,String indexId) throws IOException,ExternalServiceFailedException{
		LuceneIndex index = indexRepository.findOneByIndexId(indexId);
		if(index==null){
			String msg = String.format("The index \"%s\" does not exist.",indexId);
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		File f = FileFactory.generateOrCreateDirectoryInstance(luceneIndexPath + indexId);
		Path path = f.toPath();
		Directory dir = FSDirectory.open(path);

		/**
		 *  Code for retrieving the document that will be deleted
		 */
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = OwnQueryParser.parseDocumentIdQuery(documentId);
		TopDocs results = searcher.search(query, 1);
		Model resultModel = LuceneResultConverterToJENA.convertResults(query, searcher, results);
		reader.close();

		deleteDocument(documentId, indexId);
		
		return resultModel;
	}

	public void deleteDocument(String documentId,String indexId) throws IOException,ExternalServiceFailedException{
		LuceneIndex index = indexRepository.findOneByIndexId(indexId);
		if(index==null){
			String msg = String.format("The index \"%s\" does not exist.",indexId);
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		
		File f = FileFactory.generateOrCreateDirectoryInstance(luceneIndexPath + indexId);
		Path path = f.toPath();
		Directory dir = FSDirectory.open(path);

		/**
		 *  Code for deleting the document from the index
		 */
		IndexWriterConfig iwc = null;
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		String[] fields = index.getFields().split(";");
		String[] analyzers = index.getAnalyzers().split(";");
		if(fields.length!=analyzers.length){
			String msg = "The number of fields and analyzers is different";
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		
		for (int i = 0; i < fields.length; i++) {
//			System.out.println("GENERATING "+analyzers[i]+" for "+fields[i]);
			Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],index.getLanguage());
			analyzerMap.put(fields[i], particularAnalyzer);
		}
		PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
				AnalyzerFactory.getAnalyzer("standard", index.getLanguage()), 
				analyzerMap);
		iwc = new IndexWriterConfig(wrapper);

		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = new IndexWriter(dir, iwc);
		
		try  {
			Term term= new Term("documentId",documentId);
			writer.deleteDocuments(term);
		}
		catch (IOException e){
			writer.commit();
			writer.close();
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, "Error adding document to index::"+e.getMessage());
		}
		writer.commit();
		writer.close();
	}

	/** 
	 * Index a text file. 
	 * @param docContent
	 * @param docType
	 * @param index
	 * @param create
	 * @param sFields
	 * @param sAnalyzers
	 * @return
	 * @throws IOException
	 * @throws ExternalServiceFailedException
	 */
	public Model addDocument(Model docContent,String indexId) throws IOException,ExternalServiceFailedException{
		LuceneIndex index = indexRepository.findOneByIndexId(indexId);
		if(index==null){
			String msg = String.format("The index \"%s\" does not exist.",indexId);
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		File f = FileFactory.generateOrCreateDirectoryInstance(luceneIndexPath + indexId);
		Path path = f.toPath();
		Directory dir = FSDirectory.open(path);

		IndexWriterConfig iwc = null;
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		String[] fields = index.getFields().split(";");
		String[] analyzers = index.getAnalyzers().split(";");
		if(fields.length!=analyzers.length){
			String msg = "The number of fields and analyzers is different";
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
		}
		
		for (int i = 0; i < fields.length; i++) {
//			System.out.println("GENERATING "+analyzers[i]+" for "+fields[i]);
			Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],index.getLanguage());
			analyzerMap.put(fields[i], particularAnalyzer);
		}
		PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
				AnalyzerFactory.getAnalyzer("standard", index.getLanguage()), 
				analyzerMap);
		iwc = new IndexWriterConfig(wrapper);

		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// Optional: for better indexing performance, if you are indexing many documents, increase the RAM
		// buffer.  But if you do this, increase the max heap size to the JVM (eg add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);

		IndexWriter writer = new IndexWriter(dir, iwc);
		IDocumentParser documentParser;
		try {
			documentParser = DocumentParserFactory.getDocumentParser("nif");
		} catch (UnSupportedDocumentParserFormatException e1) {
			documentParser = new NIFDocumentParser();
		}
		NIFWriter.addLuceneIndexingInformation(docContent, indexId, luceneIndexPath);
		Document doc = documentParser.parseDocumentFromModel(docContent,fields);
		try  {
			writer.addDocument(doc);
		}
		catch (IOException e){
			writer.close();
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, "Error adding document to index::"+e.getMessage());
		}
		writer.commit();
		writer.close();
		return docContent;
	}

	/**
	 * Searches a query against a field of an index and return hitsToReturn documents.
	 * @param index index where to search for the query text
	 * @param field document field against what to match the query
	 * @param queryString text of the input query
	 * @param hitsToReturn number of documents to be returned
	 * @return JSON format string containing the results information and content
	 * @throws ExternalServiceFailedException
	 */
	public Model search(String indexId,String queryString, int hitsToReturn) throws ExternalServiceFailedException {
		try{
			LuceneIndex index = indexRepository.findOneByIndexId(indexId);
			if(index==null){
				String msg = String.format("The index \"%s\" does not exist.",indexId);
	        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
			}
			File f = FileFactory.generateFileInstance(luceneIndexPath + indexId);
			if(f==null || !f.exists()){
				throw new ExternalServiceFailedException("Specified index ["+luceneIndexPath + indexId+"] does not exists.");
			}
			logger.info("Searching in folder: "+f.getAbsolutePath());
			Path path = f.toPath();
			Directory dir = FSDirectory.open(path);
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			
//			Document doc = reader.document(0);
			String[] fields = index.getFields().split(";");
			String[] analyzers = index.getAnalyzers().split(";");
			if(fields.length!=analyzers.length){
				logger.error("The number of fields and analyzers is different");
				throw new BadRequestException("The number of fields and analyzers is different");
			}
			Query query = OwnQueryParser.parseQuery(queryString, fields, analyzers, index.getLanguage());
			
			TopDocs results = searcher.search(query, hitsToReturn);
//			Explanation exp = searcher.explain(query, 0);
			Model resultModel = LuceneResultConverterToJENA.convertResults(query, searcher, results);
			reader.close();
			return resultModel;
		}
		catch(IOException e){
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
		}
	}

	public String getIndexDirectory() {
		return luceneIndexPath;
	}

	public void setIndexDirectory(String indexDirectory) {
		this.luceneIndexPath = indexDirectory;
	}


	public boolean isIndexCreate() {
		return indexCreate;
	}

	public void setIndexCreate(boolean indexCreate) {
		this.indexCreate = indexCreate;
	}
	
}
