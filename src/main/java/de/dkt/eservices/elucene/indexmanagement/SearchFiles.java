/**
 * Copyright (C) 2015 3pc, Art+Com, Condat, Deutsches Forschungszentrum 
 * für Künstliche Intelligenz, Kreuzwerke (http://)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.elucene.indexmanagement.queryparser.OwnQueryParser;
import de.dkt.eservices.elucene.indexmanagement.resultconverter.JSONLuceneResultConverter;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * Configures searching files in Lucene
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class SearchFiles {

	static Logger logger = Logger.getLogger(SearchFiles.class);

	private static Version luceneVersion = Version.LUCENE_3_0;
	
	private static String indexDirectory  ="/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/";
	
	private SearchFiles() {}

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {
		SearchFiles.indexDirectory  ="/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/luceneindexes/";
		String result = SearchFiles.search("test1", "content", "standard", "plaintext", "Madrid","en",10);
		System.out.println(result);
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
	public static String search(String index,String sFields, String sAnalyzers, String queryType, String queryString, String language, int hitsToReturn) throws ExternalServiceFailedException {
		try{
			System.out.println(index+"__"+sFields+"__"+sAnalyzers+"__"+queryType+"__"+language+"__"+hitsToReturn);
			System.out.println(indexDirectory);
			Date start = new Date();

			File f = FileFactory.generateFileInstance(indexDirectory + index);
			if(f==null || !f.exists()){
				throw new ExternalServiceFailedException("Specified index ["+indexDirectory + index+"] does not exists.");
			}
			logger.info("Searching in folder: "+f.getAbsolutePath());
			Directory dir = FSDirectory.open(f);
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			
			Document doc = reader.document(0);
			System.out.println(reader.numDocs());
			System.out.println(doc);
			
			String[] fields = sFields.split(";");
			String[] analyzers = sAnalyzers.split(";");
			if(fields.length!=analyzers.length){
				logger.error("The number of fields and analyzers is different");
				throw new BadRequestException("The number of fields and analyzers is different");
			}
			
System.out.println("CHECK IF THE QUERY IS WORKING PROPERLY: "+queryString);
			
			Query query = OwnQueryParser.parseQuery(queryType, queryString, fields, analyzers, language);

System.out.println("\t QUERY: "+query);

			TopDocs results = searcher.search(query, hitsToReturn);

			Explanation exp = searcher.explain(query, 0);
			System.out.println("EXPLANATION: "+exp);

			System.out.println("TOTAL HITS: " + results.totalHits);
			
			Date end = new Date();
			logger.info("Time: "+(end.getTime()-start.getTime())+"ms");
//			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");

			String resultString = JSONLuceneResultConverter.convertResults(query, searcher, results);
			reader.close();
			return resultString;
		}
		catch(IOException e){
			e.printStackTrace();
			throw new ExternalServiceFailedException("IOException with message: "+e.getMessage());
		}
	}

	public static String getIndexDirectory() {
		return indexDirectory;
	}

	public static void setIndexDirectory(String indexDirectory) {
		SearchFiles.indexDirectory = indexDirectory;
	}
	
	
}
