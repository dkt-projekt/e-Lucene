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
package eu.freme.broker.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import eu.freme.broker.elucene.exceptions.BadRequestException;
import eu.freme.broker.elucene.exceptions.ExternalServiceFailedException;
import eu.freme.broker.elucene.indexmanagement.analyzer.AnalyzerFactory;
import eu.freme.broker.elucene.indexmanagement.resultconverter.JSONLuceneResultConverter;

/**
 * Configures searching files in Lucene
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class SearchFiles {

	static Logger logger = Logger.getLogger(SearchFiles.class);

	static Version luceneVersion = Version.LUCENE_4_9;
	
	private SearchFiles() {}

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {
//		String result = SearchFiles.search("index1", "field", "lucene capabilities", 5);
//		System.out.println(result);
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
	public static String search(String index,String sFields, String sAnalyzers, String queryString, int hitsToReturn) throws ExternalServiceFailedException {
		try{
			String indexDirectory = "indexes/";
			Date start = new Date();

			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
				
			Resource indexResource = ctx.getResource("classpath:"+indexDirectory + index);
			if(!indexResource.exists()){
				ctx.close();
				throw new ExternalServiceFailedException("Specified index does not exists.");
			}

			File f = indexResource.getFile();
			Directory dir = FSDirectory.open(f);
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);

			ctx.close();
			
//			System.out.println(reader.numDocs());
//			System.out.println(reader.document(0).toString());
			String[] fields = sFields.split(";");
			String[] analyzers = sAnalyzers.split(";");
			if(fields.length!=analyzers.length){
				logger.error("The number of fields and analyzers is different");
				throw new BadRequestException("The number of fields and analyzers is different");
			}
			
			BooleanQuery booleanQuery = new BooleanQuery();
			
			if(fields.length==1){
				Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], null, luceneVersion);
				QueryParser parser1 = new QueryParser(Version.LUCENE_4_9,"title", analyzer);
				Query query1 = parser1.parse(queryString);
				booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
			}
			else{
				/**
				 * When each field has to be analyzed with a different analyzer.
				 */
				for (int i = 0; i < fields.length; i++) {
					Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],null/*language*/,luceneVersion);
					QueryParser parser1 = new QueryParser(Version.LUCENE_4_9,"title", particularAnalyzer);
					Query query1 = parser1.parse(queryString);
					booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
				}
			}
			
			TopDocs results = searcher.search(booleanQuery, hitsToReturn);
			reader.close();

			Date end = new Date();
			logger.info("Time: "+(end.getTime()-start.getTime())+"ms");
//			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");

			//TODO Add more types of output (NIF, RDF, plaintext, etc.).
			return JSONLuceneResultConverter.convertResults(booleanQuery, searcher, results);
		}
		catch(IOException e){
			e.printStackTrace();
			throw new ExternalServiceFailedException("IOException with message: "+e.getMessage());
		}
		catch (ParseException e) {
			e.printStackTrace();
			throw new ExternalServiceFailedException("ParseException with message: "+e.getMessage());
		}
	}
}
