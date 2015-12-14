package eu.freme.broker.elucene.indexmanagement.queryparser;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import com.hp.hpl.jena.rdf.model.Model;

import eu.freme.broker.elucene.indexmanagement.analyzer.AnalyzerFactory;
import eu.freme.broker.exception.ExternalServiceFailedException;
import eu.freme.broker.niftools.NIFReader;

public class OwnQueryParser {

	private static Version luceneVersion = Version.LUCENE_4_9;
	
	
	
	public static Query parseQuery(String queryType, String queryContent, String[] fields, String [] analyzers){
		BooleanQuery booleanQuery = new BooleanQuery();
		
		try{
			if(queryType.equalsIgnoreCase("plain")){
				String queryString = queryContent;
				if(fields.length==1){
					Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], null, luceneVersion);
					QueryParser parser1 = new QueryParser(Version.LUCENE_4_9,"content", analyzer);
					Query query1 = parser1.parse(queryString);
					booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
				}
				else{
					/**
					 * When each field has to be analyzed with a different analyzer.
					 */
					for (int i = 0; i < fields.length; i++) {
						Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],null/*language*/,luceneVersion);
						QueryParser parser1 = new QueryParser(Version.LUCENE_4_9,fields[i], particularAnalyzer);
						Query query1 = parser1.parse(queryString);
						booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
					}
				}
			}
			else if(queryType.equalsIgnoreCase("nif")){
				Model nifModel = NIFReader.extractModelFromString(queryContent);
				String textContent = NIFReader.extractIsString(nifModel);
				List<String[]> entities = NIFReader.extractEntities(nifModel);

				
				Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], null, luceneVersion);
				QueryParser parser1 = new QueryParser(Version.LUCENE_4_9,"content", analyzer);
				Query query1 = parser1.parse(textContent);
				booleanQuery.add(query1, BooleanClause.Occur.SHOULD);

				String entString = "";
				String tempString = "";
				for (int i = 0; i < entities.size(); i++) {
					if(entities.get(i)[2].contains("DAT")){
						tempString += ";" + entities.get(i)[0];
					}
					else{
						entString += ";" + entities.get(i)[0];
					}
				}
				tempString = (tempString.equals("")) ? tempString : tempString.substring(1);
				entString = (entString.equals("")) ? entString : entString.substring(1);

				Analyzer nerAnalyzer = AnalyzerFactory.getAnalyzer("NERAnalyzer",null/*language*/,luceneVersion);
				QueryParser parserNER = new QueryParser(Version.LUCENE_4_9,"entities", nerAnalyzer);
				Query queryNER = parserNER.parse(entString);
				booleanQuery.add(queryNER, BooleanClause.Occur.SHOULD);
				
				Analyzer tempAnalyzer = AnalyzerFactory.getAnalyzer("TEMPAnalyzer",null/*language*/,luceneVersion);
				QueryParser parserTEMP = new QueryParser(Version.LUCENE_4_9,"temporals", tempAnalyzer);
				Query queryTEMP = parserTEMP.parse(tempString);
				booleanQuery.add(queryTEMP, BooleanClause.Occur.SHOULD);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
		
		return booleanQuery;
		
	}
}
