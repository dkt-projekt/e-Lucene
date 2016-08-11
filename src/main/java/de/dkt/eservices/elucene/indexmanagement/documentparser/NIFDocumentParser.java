package de.dkt.eservices.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.elucene.ELuceneServiceStandAlone;
import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a NIF file. The lucene document contains three fields:
 * 	- Title: the first line of the TXT file.
 *  - Body: the rest of the TXT file.
 *  - Entities: the whole text = title + body.
 *  - Path: the name of the file.
 */
public class NIFDocumentParser implements IDocumentParser{

	Logger logger = Logger.getLogger(ELuceneServiceStandAlone.class);

	public NIFDocumentParser() {
	}

	@Override
	public Document parseDocumentFromFile(String path, String[] fields) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String line = br.readLine();
			String content = null;
			while(line!=null){
				if(content==null)
					content = line;
				else
					content=content + " " + line;
				line = br.readLine();
			}
			br.close();
			return parseDocumentFromString(content, fields);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Document parseDocumentFromString(String content, String[] fields) {
		try{
			Model model = null;
			try{
				model = NIFReader.extractModelFromFormatString(content, RDFSerialization.RDF_XML);
			}
			catch(Exception e){
				try{
					model = NIFReader.extractModelFromFormatString(content, RDFSerialization.TURTLE);
				}
				catch(Exception e2){
					throw new BadRequestException("String format not allowed.");
				}
			}
			return parseDocumentFromModel(model, fields);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	@Override
	public Document parseDocumentFromModel(Model model, String[] fields) {
		try {
			Document doc = new Document();
			String fields2 [] = fields;
			if(fields2==null || fields2.length==0){
				fields2 = new String []{"content","entities","temporal","path","nifpath","docURI"};
			}
			
			for (String fieldString : fields2) {
				String text = "";
				Field.Store store = null;
				if(fieldString.equalsIgnoreCase("content")){
					text = NIFReader.extractIsString(model);
					store = Store.YES;
				}
				else if(fieldString.equalsIgnoreCase("entities")){
					List<String[]> list = NIFReader.extractEntities(model);
					for (String[] strings : list) {
						if(!strings[3].contains("DAT")){
							text = text + ";" + strings[0];
						}
					}
					store = Store.YES;
				}
				else if(fieldString.equalsIgnoreCase("temporal")){
					List<String[]> list = NIFReader.extractEntities(model);
					for (String[] strings : list) {
						if(strings[3].contains("DAT")){
							text = text + ";" + strings[0];
						}
					}
					store = Store.YES;
				}
				else if(fieldString.equalsIgnoreCase("tempMean")){
				}
				else if(fieldString.equalsIgnoreCase("tempStDev")){
				}
				else if(fieldString.equalsIgnoreCase("path")){
					text = NIFReader.extractDocumentPath(model);
					store = Store.YES;
				}
				else if(fieldString.equalsIgnoreCase("nifpath")){
					text = NIFReader.extractDocumentNIFPath(model);
					store = Store.YES;
				}
				else if(fieldString.equalsIgnoreCase("docURI")){
					text = NIFReader.extractDocumentURI(model);
					store = Store.YES;
				}
				else{
					text = NIFReader.extractIsString(model);
					store = Store.YES;
				}
				doc.add(new TextField(fieldString, text, store));
			}
	//		doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
			return doc;
		} catch (Exception e) {
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
		}
	}
}
