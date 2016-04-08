package de.dkt.eservices.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.NIFReader;
import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a TXT file. The lucene document contains three fields:
 * 	- Title: the first line of the TXT file.
 *  - Body: the rest of the TXT file.
 *  - Entities: the whole text = title + body.
 *  - Path: the name of the file.
 */
public class DktDocumentParser implements IDocumentParser{

	RDFConversionService rdfconversion;
	
	public DktDocumentParser() {
		rdfconversion  = new JenaRDFConversionService();
	}
	
	@Override
	public Document parseDocumentFromFile(String path, String[] fields) {
		//TODO
		return null;
	}

	@Override
	public Model parseModelFromFile(String content, String[] fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document parseDocumentFromString(String content, String[] fields) {
		try{
			Document doc = new Document();
	
			String fields2 [] = fields;
			if(fields2==null || fields2.length==0){
				fields2 = new String []{"text","docId"};
			}
			
			String parts[] = content.split("\t");
			for (String fieldString : fields2) {
				String text = "";
				Field.Store store = Store.YES;
				if(fieldString.equalsIgnoreCase("text")){
//					text = parts[1];
//					store = Store.YES;
//					doc.add(new TextField(fieldString, text, store));
					FieldType type = new FieldType();
					type.setIndexed(true);
					type.setStored(true);
					type.setStoreTermVectors(true);
					Field field = new Field("text", parts[1], type);
					doc.add(field);
				}
				else if(fieldString.equalsIgnoreCase("docId")){
					text = parts[0];
					store = Store.YES;
					doc.add(new StringField(fieldString, text, store));
				}
//				doc.add(new TextField(fieldString, text, store));
			}
			return doc;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

}
