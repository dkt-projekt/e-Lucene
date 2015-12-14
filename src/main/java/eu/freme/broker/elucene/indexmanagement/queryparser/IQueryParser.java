package eu.freme.broker.elucene.indexmanagement.queryparser;

import org.apache.lucene.search.Query;

public interface IQueryParser {

	public Query parseQuery(String text);
	
}
