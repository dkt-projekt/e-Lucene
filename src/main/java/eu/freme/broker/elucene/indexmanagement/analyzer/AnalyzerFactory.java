package eu.freme.broker.elucene.indexmanagement.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * @author jmschnei
 *
 */
public class AnalyzerFactory {

	public static Analyzer getAnalyzer(String analyzer,String language,Version matchVersion){
		
		if(analyzer.equalsIgnoreCase("NERAnalyzer")){
			return new NERAnalyzer();
		}
		if(analyzer.equalsIgnoreCase("TEMPAnalyzer")){
			return new TEMPAnalyzer();
		}
		if(language.equalsIgnoreCase("spanish") || language.equalsIgnoreCase("es") || language.equalsIgnoreCase("spa")){
			if(analyzer.equalsIgnoreCase("standard")){
				return new SpanishAnalyzer(matchVersion);
			}
//			else if(analyzer.equalsIgnoreCase("entity")){
//			return new EntitiesAnalyzer(matchVersion, language);
//			}
//			else if(analyzer.equalsIgnoreCase("temporal")){
//				return new TemporalAnalyzer(matchVersion, language);
//			}
		}
		if(language.equalsIgnoreCase("german") || language.equalsIgnoreCase("de") || language.equalsIgnoreCase("ger")){
			return new SpanishAnalyzer(matchVersion);
		}
		if(language.equalsIgnoreCase("english") || language.equalsIgnoreCase("en") || language.equalsIgnoreCase("eng")){
			if(analyzer.equalsIgnoreCase("standard")){
				return new StandardAnalyzer(matchVersion);
			}
		}
		return new StandardAnalyzer(matchVersion);
	}
}
