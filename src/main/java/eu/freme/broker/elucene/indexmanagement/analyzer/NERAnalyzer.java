package eu.freme.broker.elucene.indexmanagement.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class NERAnalyzer extends Analyzer{

	/* This is the only function that we need to override for our analyzer.
	 * It takes in a java.io.Reader object and saves the tokenizer and list
	 * of token filters that operate on it. 
	 */
	@Override
	protected TokenStreamComponents createComponents(String field,Reader reader) {
		Tokenizer tokenizer = new SemicolonTokenizer(reader);
//		TokenStream filter = new EmptyStringTokenFilter(tokenizer);
//		filter = new LowerCaseFilte(filter);
		return new TokenStreamComponents(tokenizer, null);
	}
}
