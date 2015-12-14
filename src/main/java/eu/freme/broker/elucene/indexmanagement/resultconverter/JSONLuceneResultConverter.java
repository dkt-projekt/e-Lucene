package eu.freme.broker.elucene.indexmanagement.resultconverter;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.freme.broker.exception.ExternalServiceFailedException;

public class JSONLuceneResultConverter {
	
	static Logger logger = Logger.getLogger(JSONLuceneResultConverter.class);
	
	public static String convertResults(Query query,IndexSearcher searcher,TopDocs results) throws ExternalServiceFailedException{
		try{
			ScoreDoc[] hits = results.scoreDocs;
			int numTotalHits = results.totalHits;
			System.out.println(numTotalHits + " total matching documents");
			JSONObject obj = new JSONObject();
			obj.put("querytext", query.toString());
			obj.put("indexName", "TODO include name of the index");
			JSONArray list = new JSONArray();
	
			for (int i = 0; i < numTotalHits; i++) {
				JSONObject resultJSON = new JSONObject();
				Document doc = searcher.doc(hits[i].doc);
	//			String path = doc.get("path");
	//			if (path != null) {
	//				System.out.println((i+1) + ". " + path);
	//				String title = doc.get("title");
	//				if (title != null) {
	//					System.out.println("   Title: " + doc.get("title"));
	//				}
	//			} else {
	//				System.out.println((i+1) + ". " + "No path for this document");
	//			}
				resultJSON.put("identification", i+1);
				resultJSON.put("title", doc.get("title"));
				resultJSON.put("body", doc.get("body"));
				resultJSON.put("entities", doc.get("entities"));
				resultJSON.put("score", hits[i].score);
				list.put(resultJSON);
			}
			obj.put("documents", list);
			
			return obj.toString();
		}
		catch(Exception e){
			logger.error("Error at converting LUCENE output to JSON");
			throw new ExternalServiceFailedException("Error at converting LUCENE output to JSON");
		}
	}
}
