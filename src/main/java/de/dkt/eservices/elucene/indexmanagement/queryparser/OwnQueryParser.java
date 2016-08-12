package de.dkt.eservices.elucene.indexmanagement.queryparser;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.elucene.indexmanagement.analyzer.AnalyzerFactory;
import eu.freme.common.exception.ExternalServiceFailedException;

public class OwnQueryParser {

	private static Version luceneVersion = Version.LUCENE_4_9;
	
	public static Query parseQuery(String queryContent, String[] fields, String [] analyzers, String language){
		BooleanQuery booleanQuery = new BooleanQuery();
		try{
			String queryString = queryContent;
			if(fields==null || fields.length==0 || (fields.length==1 && fields[0].equalsIgnoreCase("all")) ){
				fields = new String []{"content","entities","links","temporal","nifcontent","docURI"};
				analyzers = new String []{"standard","standard","standard","standard","standard","standard"};
			}
			for (int i = 0; i < fields.length; i++) {
				Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language,luceneVersion);
				QueryParser parser1 = new QueryParser(luceneVersion,fields[i], particularAnalyzer);
				Query query1 = parser1.parse(queryString);
				booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
		return booleanQuery;
	}

	public static Query parseComplexQuery(String queryType, String queryContent, String[] fields, String [] analyzers, String language){
		BooleanQuery booleanQuery = new BooleanQuery();
		try{
			if(queryType.equalsIgnoreCase("nif")){
				Model nifModel = NIFReader.extractModelFromString(queryContent);
				String textContent = NIFReader.extractIsString(nifModel);
				List<String[]> entities = NIFReader.extractEntities(nifModel);
				
				Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], language, luceneVersion);
				QueryParser parser1 = new QueryParser(luceneVersion,"content", analyzer);
				Query query1 = parser1.parse(textContent);
				booleanQuery.add(query1, BooleanClause.Occur.SHOULD);

				String entString = "";
				String tempString = "";
				for (int i = 0; i < entities.size(); i++) {
					if(entities.get(i)[2].contains("DAT")){
						
						
						tempString += " " + entities.get(i)[0];
					}
					else{
						entString += " " + entities.get(i)[0];
					}
				}
				entString = scapeStringForLuceneQuery(entString);
				tempString = scapeStringForLuceneQuery(tempString);
				tempString = (tempString.equals("")) ? tempString : tempString.substring(1);
				entString = (entString.equals("")) ? entString : entString.substring(1);
				
//				if(!entString.equals("")){
//					//Analyzer nerAnalyzer = AnalyzerFactory.getAnalyzer("NERAnalyzer",language,luceneVersion);
////					QueryParser parserNER = new QueryParser(luceneVersion,"entities", nerAnalyzer);
//					QueryParser parserNER = new QueryParser(luceneVersion,"entities", new WhitespaceAnalyzer(luceneVersion));
////					System.out.println("ENTSTRING: "+entString);
//					Query queryNER = parserNER.parse(entString);
////					System.out.println("QUERY: "+queryNER.toString());
//					booleanQuery.add(queryNER, BooleanClause.Occur.SHOULD);
//				}
//				
//				if(!tempString.equals("")){
////					Analyzer tempAnalyzer = AnalyzerFactory.getAnalyzer("TEMPAnalyzer",language,luceneVersion);
////					QueryParser parserTEMP = new QueryParser(luceneVersion,"temporals", tempAnalyzer);
//					QueryParser parserTEMP = new QueryParser(luceneVersion,"temporals", new WhitespaceAnalyzer(luceneVersion));
//					Query queryTEMP = parserTEMP.parse(tempString);
//					booleanQuery.add(queryTEMP, BooleanClause.Occur.SHOULD);
//				}
			}
			else if(queryType.equalsIgnoreCase("docid") || queryType.equalsIgnoreCase("tfidf") || queryType.equalsIgnoreCase("plaintext") ){
				String queryString = queryContent;
				if(fields.length==1){
					String field = (queryType.equalsIgnoreCase("plaintext")) ? "content" : fields[0];
					Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], language, luceneVersion);
					QueryParser parser1 = new QueryParser(luceneVersion,field, analyzer);
					Query query1 = parser1.parse(queryString);
					booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
				}
				else{
					for (int i = 0; i < fields.length; i++) {
						Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language,luceneVersion);
						QueryParser parser1 = new QueryParser(luceneVersion,fields[i], particularAnalyzer);
						Query query1 = parser1.parse(queryString);
						booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
		return booleanQuery;
	}

	public static String scapeStringForLuceneQuery(String s){
		s = s.replaceAll("\\\\", "\\\\\\\\");
		s = s.replaceAll("\\+", "\\\\\\+");
		s = s.replaceAll("\\-", "\\\\\\-");
		s = s.replaceAll("&", "\\\\&");
		s = s.replaceAll("\\|", "\\\\\\|");
		s = s.replaceAll("!", "\\\\!");
		s = s.replaceAll("\\(", "\\\\\\(");
		s = s.replaceAll("\\)", "\\\\\\)");
		s = s.replaceAll("\\[", "\\\\\\[");
		s = s.replaceAll("\\]", "\\\\\\]");
		s = s.replaceAll("\\{", "\\\\\\{");
		s = s.replaceAll("\\}", "\\\\\\}");
		s = s.replaceAll("\\^", "\\\\\\^");
		s = s.replaceAll("\"", "\\\\\"");
		s = s.replaceAll("~", "\\\\~");
		s = s.replaceAll("\\*", "\\\\\\*");
		s = s.replaceAll("\\?", "\\\\\\?");
		s = s.replaceAll(":", "\\\\:");
		s = s.replaceAll("/", "\\\\/");
		return s;
	}
}
