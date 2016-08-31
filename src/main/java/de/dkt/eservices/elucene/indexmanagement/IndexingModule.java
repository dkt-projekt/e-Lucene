package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.filemanagement.FileFactory;
import de.dkt.common.niftools.NIFWriter;
import de.dkt.eservices.elucene.indexmanagement.analyzer.AnalyzerFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.DocumentParserFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.IDocumentParser;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * Configures Lucene indexing files
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
@Component
public class IndexingModule {

	static Logger logger = Logger.getLogger(IndexingModule.class);
	
	@Value("${luceneIndexPath}")
	private String luceneIndexPath;
	private boolean indexCreate = false;

	private IndexingModule() {}

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
	public Model indexModel(Model docContent,String index, String sFields, String sAnalyzers, String language) throws IOException,ExternalServiceFailedException{
		Date start = new Date();
		logger.info("Indexing to directory '" + luceneIndexPath + index + "'...");

		File f = FileFactory.generateOrCreateDirectoryInstance(luceneIndexPath + index);
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

		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// Optional: for better indexing performance, if you are indexing many documents, increase the RAM
		// buffer.  But if you do this, increase the max heap size to the JVM (eg add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);

		IndexWriter writer = new IndexWriter(dir, iwc);
		
		IDocumentParser documentParser = DocumentParserFactory.getDocumentParser("nif");
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

		Date end = new Date();
		logger.info(end.getTime() - start.getTime() + " total milliseconds");

		NIFWriter.addLuceneIndexingInformation(docContent, index, luceneIndexPath);
		return docContent;
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
