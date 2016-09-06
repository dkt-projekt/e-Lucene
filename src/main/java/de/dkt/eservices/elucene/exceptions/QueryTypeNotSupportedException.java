package de.dkt.eservices.elucene.exceptions;

@SuppressWarnings("serial")
public class QueryTypeNotSupportedException extends Exception{

	public QueryTypeNotSupportedException(){
		super("The given query type is not supported");
	}
}
