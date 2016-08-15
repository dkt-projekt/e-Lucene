package de.dkt.eservices.elucene.indexmanagement.analyzer;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;

public class TEMPAnalyzer extends Analyzer{

	/* This is the only function that we need to override for our analyzer.
	 * It takes in a java.io.Reader object and saves the tokenizer and list
	 * of token filters that operate on it. 
	 */
	@Override
	protected TokenStreamComponents createComponents(String text) {
		StringReader reader = new StringReader(text);
		Tokenizer tokenizer = new SemicolonTokenizer(reader);
		TokenStream filter = new TemporalNormalizationTokenFilter(tokenizer);
		filter = new LowerCaseFilter(filter);
		return new TokenStreamComponents(tokenizer, filter);
	}
}
