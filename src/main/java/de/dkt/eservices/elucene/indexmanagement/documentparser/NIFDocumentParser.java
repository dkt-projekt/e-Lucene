package de.dkt.eservices.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.niftools.ITSRDF;
import de.dkt.common.niftools.NIF;
import de.dkt.common.niftools.NIFReader;
import de.dkt.common.niftools.TIME;
import de.dkt.eservices.elucene.ELuceneRestController;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a NIF file. The lucene document contains three fields:
 */
public class NIFDocumentParser implements IDocumentParser{

	Logger logger = Logger.getLogger(ELuceneRestController.class);

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
				model = NIFReader.extractModelFromFormatString(content, RDFSerialization.TURTLE);
			}
			catch(Exception e){
				throw new BadRequestException("Input String format not supported: only turtle is supported.");
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
			if(fields2==null || fields2.length==0 || (fields.length==1 && fields[0].equalsIgnoreCase("all")) ){
				fields2 = new String []{"content","entities","links","temporal","nifcontent","docURI"};
			}
			
			for (String fieldString : fields2) {
				String text = "";
				if(fieldString.equalsIgnoreCase("content")){
					text = NIFReader.extractIsString(model);
					doc.add(new TextField(fieldString, text, Store.YES));
				}
				else if(fieldString.equalsIgnoreCase("entities")){
					Map<String,Map<String,String>> nonTemporalExpressionsEntities = NIFReader.extractNonTemporalEntitiesExtended(model);
					if(nonTemporalExpressionsEntities!=null){
						Set<String> nonTemporalExpressionsKeyset = nonTemporalExpressionsEntities.keySet();
						for (String nteKey : nonTemporalExpressionsKeyset) {
							Map<String,String> nteProperties = nonTemporalExpressionsEntities.get(nteKey);
							Set<String> ntePropertiesKeyset = nteProperties.keySet();
							for (String propertyKey : ntePropertiesKeyset) {
								if(propertyKey.equalsIgnoreCase(NIF.anchorOf.getURI())){
									text = text + ";" + nteProperties.get(propertyKey);
								}
							}
						}
					}
					text = (text.length()>0)?text.substring(1):"";
					doc.add(new TextField(fieldString, text, Store.YES));
				}
				else if(fieldString.equalsIgnoreCase("links")){
					Map<String,Map<String,String>> nonTemporalExpressionsEntities = NIFReader.extractNonTemporalEntitiesExtended(model);
					if(nonTemporalExpressionsEntities!=null){
						Set<String> nonTemporalExpressionsKeyset = nonTemporalExpressionsEntities.keySet();
						for (String nteKey : nonTemporalExpressionsKeyset) {
							Map<String,String> nteProperties = nonTemporalExpressionsEntities.get(nteKey);
							Set<String> ntePropertiesKeyset = nteProperties.keySet();
							for (String propertyKey : ntePropertiesKeyset) {
								if(propertyKey.equalsIgnoreCase(ITSRDF.taIdentRef.getURI())){
									text = text + ";" + nteProperties.get(propertyKey);
								}
							}
						}
					}
					text = (text.length()>0)?text.substring(1):"";
					doc.add(new TextField(fieldString, text, Store.YES));
				}
				else if(fieldString.equalsIgnoreCase("temporal")){
					Map<String,Map<String,String>> temporalExpressionsEntities = NIFReader.extractTemporalEntitiesExtended(model);
					if(temporalExpressionsEntities!=null){
						Set<String> temporalExpressionsKeyset = temporalExpressionsEntities.keySet();
						for (String teKey : temporalExpressionsKeyset) {
							Map<String,String> teProperties = temporalExpressionsEntities.get(teKey);
							Set<String> tePropertiesKeyset = teProperties.keySet();
							String initialTime = "";
							String finalTime = "";
							for (String propertyKey : tePropertiesKeyset) {
								if(propertyKey.equalsIgnoreCase(TIME.intervalStarts.getURI())){
									initialTime = teProperties.get(propertyKey);
								}
								if(propertyKey.equalsIgnoreCase(TIME.intervalFinishes.getURI())){
									finalTime = teProperties.get(propertyKey);
								}
							}
							text = text + ";" + initialTime+"_"+finalTime;
						}
					}
					text = (text.length()>0)?text.substring(1):"";
					doc.add(new TextField(fieldString, text, Store.YES));
				}
				else if(fieldString.equalsIgnoreCase("nifContent")){
					text = NIFReader.model2String(model, RDFSerialization.TURTLE);
					doc.add(new StoredField(fieldString, text));
				}
				else if(fieldString.equalsIgnoreCase("docURI")){
					text = NIFReader.extractDocumentWholeURI(model);
					doc.add(new StoredField(fieldString, text));
				}
				else if(fieldString.equalsIgnoreCase("tempMean")){
				}
				else if(fieldString.equalsIgnoreCase("tempStDev")){
				}
				else if(fieldString.equalsIgnoreCase("geoLong")){
				}
				else if(fieldString.equalsIgnoreCase("geoLat")){
				}
				else{
					text = NIFReader.extractIsString(model);
					doc.add(new TextField(fieldString, text, Store.YES));
				}
			}
	//		doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
		}
	}
}
