package de.dkt.eservices.elucene.indexmanagement.documentparser;

import de.dkt.eservices.elucene.exceptions.UnSupportedDocumentParserFormatException;

/**
 * @author jmschnei
 *
 */
public class DocumentParserFactory {

	public static IDocumentParser getDocumentParser(String type) 
			throws UnSupportedDocumentParserFormatException{
		if(type==null || type.equals("")){
			throw new UnSupportedDocumentParserFormatException();
		}
		
//		if(type.equalsIgnoreCase("xml")){
//			return XMLDocumentParser.parseDocument(path);
//		}
		if(type.equalsIgnoreCase("txt")){
			return new TXTDocumentParser();
		}
//		if(type.equalsIgnoreCase("json")){
//			return JSONDocumentParser.parseDocument(path);
//		}
		if(type.equalsIgnoreCase("nif")){
			return new NIFDocumentParser();
		}
//		if(type.equalsIgnoreCase("turtle")){
//			return TurtleDocumentParser.parseDocument(path);
//		}
//		if(type.equalsIgnoreCase("pdf")){
//			return PDFDocumentParser.parseDocument(path);
//		}
//		if(type.equalsIgnoreCase("doc")){
//		return DOCDocumentParser.parseDocument(path);
//	}
		throw new UnSupportedDocumentParserFormatException();
	}
}
