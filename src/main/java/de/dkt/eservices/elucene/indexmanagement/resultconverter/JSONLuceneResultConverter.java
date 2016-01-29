package de.dkt.eservices.elucene.indexmanagement.resultconverter;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.json.JSONArray;
import org.json.JSONObject;

import eu.freme.common.exception.ExternalServiceFailedException;

public class JSONLuceneResultConverter {
	
	static Logger logger = Logger.getLogger(JSONLuceneResultConverter.class);
	
	public static String convertResults(Query query,IndexSearcher searcher,TopDocs results) throws ExternalServiceFailedException{
		try{
			ScoreDoc[] hits = results.scoreDocs;
			int numTotalHits = results.totalHits;
//			System.out.println(numTotalHits + " total matching documents");
			JSONObject obj = new JSONObject();
			JSONArray listResults = new JSONArray();
			JSONObject joResults = new JSONObject();
			listResults.put(new JSONObject().put("querytext", query.toString()));
			listResults.put(new JSONObject().put("numberResults", numTotalHits));
			joResults.put("querytext", query.toString());
			joResults.put("numberResults", numTotalHits);
//			listResults.put(new JSONObject().put("", ));
			
			JSONObject joDocuments = new JSONObject();
			for (int i = 0; i < numTotalHits; i++) {
				JSONObject resultJSON = new JSONObject();
				Document doc = searcher.doc(hits[i].doc);
				resultJSON.put("docId", i+1);
				resultJSON.put("path", doc.get("path"));
				resultJSON.put("pathnif", doc.get("pathnif"));
				resultJSON.put("docURI", doc.get("docURI"));
				resultJSON.put("content", doc.get("content"));
				resultJSON.put("entities", doc.get("entities"));
				resultJSON.put("temporals", doc.get("temporals"));
				resultJSON.put("score", hits[i].score);
				joDocuments.put("document"+(i+1),resultJSON);
			}
	
//			for (int i = 0; i < numTotalHits; i++) {
//				JSONObject resultJSON = new JSONObject();
//				Document doc = searcher.doc(hits[i].doc);
//				resultJSON.put("docId", i+1);
//				resultJSON.put("path", doc.get("path"));
//				resultJSON.put("pathnif", doc.get("pathnif"));
//				resultJSON.put("docURI", doc.get("docURI"));
//				resultJSON.put("content", doc.get("content"));
//				resultJSON.put("entities", doc.get("entities"));
//				resultJSON.put("temporals", doc.get("temporals"));
//				resultJSON.put("score", hits[i].score);
//				joDocuments.put("document"+(i+1000),resultJSON);
//			}
			
			joResults.put("documents", joDocuments);
//			listResults.put(new JSONObject().put("documents", listDocuments));
//			obj.put("results", listResults);
			obj.put("results", joResults);
			return obj.toString();
		}
		catch(Exception e){
			logger.error("Error at converting LUCENE output to JSON");
			e.printStackTrace();
			throw new ExternalServiceFailedException("Error at converting LUCENE output to JSON");
		}
	}
}
