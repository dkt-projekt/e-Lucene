package de.dkt.eservices.elucene.exceptions;

import org.springframework.http.HttpStatus;

import eu.freme.common.exception.FREMEHttpException;

@SuppressWarnings("serial")
public class LuceneIndexExistsException extends FREMEHttpException{

	public LuceneIndexExistsException(){
		super("A Lucene Index with the given name already exists", HttpStatus.BAD_REQUEST);
	}
}
