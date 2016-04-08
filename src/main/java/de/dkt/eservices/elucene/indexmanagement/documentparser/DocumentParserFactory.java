package de.dkt.eservices.elucene.indexmanagement.documentparser;

import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author jmschnei
 *
 */
public class DocumentParserFactory {

	public static IDocumentParser getDocumentParser(String type) throws ExternalServiceFailedException{
		if(type==null || type.equals("")){
			//System.out.println("ERROR: type argument is null or empty");
			throw new ExternalServiceFailedException("ERROR: type argument is null or empty");
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
	if(type.equalsIgnoreCase("DktDocument")){
		return new DktDocumentParser();
	}
		System.out.println("ERROR: Unsupported document type");
		return null;
	}
}
