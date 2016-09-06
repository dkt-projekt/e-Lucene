package de.dkt.eservices.elucene.exceptions;

@SuppressWarnings("serial")
public class UnSupportedDocumentParserFormatException extends Exception{

	public UnSupportedDocumentParserFormatException(){
		super("The given document parser format is not supported");
	}
}
