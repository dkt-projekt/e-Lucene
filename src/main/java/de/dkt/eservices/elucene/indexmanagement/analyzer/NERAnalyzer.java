package de.dkt.eservices.elucene.indexmanagement.analyzer;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;

public class NERAnalyzer extends Analyzer{

	@Override
	protected TokenStreamComponents createComponents(String text) {
		StringReader sr = new StringReader(text);
		Tokenizer tokenizer = new SemicolonTokenizer(sr);
		TokenStream filter = new LowerCaseFilter(tokenizer);
		return new TokenStreamComponents(tokenizer, filter);
	}

}
