package de.dkt.eservices.elucene.indexmanagement.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * @author jmschnei
 *
 */
public class AnalyzerFactory {

	public static Analyzer getAnalyzer(String analyzer,String language){
		
		if(analyzer.equalsIgnoreCase("NERAnalyzer")){
			return new NERAnalyzer();
		}
		if(analyzer.equalsIgnoreCase("TEMPAnalyzer")){
			return new TEMPAnalyzer();
		}
		if(analyzer.equalsIgnoreCase("WhiteSpace")){
			return new WhitespaceAnalyzer();
		}
		if(language.equalsIgnoreCase("spanish") || language.equalsIgnoreCase("es") || language.equalsIgnoreCase("spa")){
			if(analyzer.equalsIgnoreCase("standard")){
				return new SpanishAnalyzer();
			}
//			else if(analyzer.equalsIgnoreCase("entity")){
//			return new EntitiesAnalyzer(matchVersion, language);
//			}
//			else if(analyzer.equalsIgnoreCase("temporal")){
//				return new TemporalAnalyzer(matchVersion, language);
//			}
		}
		if(language.equalsIgnoreCase("german") || language.equalsIgnoreCase("de") || language.equalsIgnoreCase("ger")){
			return new GermanAnalyzer();
		}
		if(language.equalsIgnoreCase("english") || language.equalsIgnoreCase("en") || language.equalsIgnoreCase("eng")){
			if(analyzer.equalsIgnoreCase("standard")){
				return new StandardAnalyzer();
			}
		}
		return new StandardAnalyzer();
	}
}
