package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.elucene.indexmanagement.queryparser.OwnQueryParser;
import de.dkt.eservices.elucene.indexmanagement.resultconverter.LuceneResultConverterToJENA;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * Configures searching files in Lucene
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class SearchingModule {
	static Logger logger = Logger.getLogger(SearchingModule.class);
	private static String indexDirectory  ="/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/";
	
	private SearchingModule() {
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
	public static Model search(String index,String sFields, String sAnalyzers, String queryType, String queryString, String language, int hitsToReturn) throws ExternalServiceFailedException {
		try{
//			System.out.println(index+"__"+sFields+"__"+sAnalyzers+"__"+queryType+"__"+language+"__"+hitsToReturn);
//			System.out.println(indexDirectory);
			Date start = new Date();

			File f = FileFactory.generateFileInstance(indexDirectory + index);
			if(f==null || !f.exists()){
				throw new ExternalServiceFailedException("Specified index ["+indexDirectory + index+"] does not exists.");
			}
			logger.info("Searching in folder: "+f.getAbsolutePath());
			Path path = f.toPath();
			Directory dir = FSDirectory.open(path);
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			
//			Document doc = reader.document(0);
			String[] fields = sFields.split(";");
			String[] analyzers = sAnalyzers.split(";");
			if(fields.length!=analyzers.length){
				logger.error("The number of fields and analyzers is different");
				throw new BadRequestException("The number of fields and analyzers is different");
			}
			Query query = OwnQueryParser.parseQuery(queryString, fields, analyzers, language);
			
			TopDocs results = searcher.search(query, hitsToReturn);
//			Explanation exp = searcher.explain(query, 0);

			Date end = new Date();
			logger.info("Time: "+(end.getTime()-start.getTime())+"ms");
//			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
//			JSONObject resultModel = JSONLuceneResultConverter.convertResults(query, searcher, results);
			Model resultModel = LuceneResultConverterToJENA.convertResults(query, searcher, results);
			reader.close();
			return resultModel;
		}
		catch(IOException e){
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
		}
	}

	public static String getIndexDirectory() {
		return indexDirectory;
	}

	public static void setIndexDirectory(String indexDirectory) {
		SearchingModule.indexDirectory = indexDirectory;
	}
}
