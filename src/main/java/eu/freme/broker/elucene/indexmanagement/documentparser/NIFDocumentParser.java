package eu.freme.broker.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.rdf.model.Model;

import eu.freme.broker.niftools.NIFReader;
import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a TXT file. The lucene document contains three fields:
 * 	- Title: the first line of the TXT file.
 *  - Body: the rest of the TXT file.
 *  - Entities: the whole text = title + body.
 *  - Path: the name of the file.
 */
public class NIFDocumentParser implements IDocumentParser{

	RDFConversionService rdfconversion;
	
	public NIFDocumentParser() {
		rdfconversion  = new JenaRDFConversionService();
	}
	
	@Override
	public Document parseDocumentFromFile(String path, String[] fields) {
		try{
			//For spring approach the next lines must be used
			//ClassPathResource cpr = new ClassPathResource("classpath:"+path);
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cpr.getFile()), "utf-8"));
			Document doc = new Document();

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

			Model model = rdfconversion.unserializeRDF(content, RDFSerialization.RDF_XML);
			
			String fields2 [] = fields;
			if(fields2==null || fields2.length==0){
				fields2 = new String []{"content","entities","temporal","path","nifpath","docURI"};
			}
			
			for (String fieldString : fields2) {
				String text = "";
				Field.Store store;
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
//			doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
			return doc;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Document parseDocumentFromString(String content, String[] fields) {
		// TODO Auto-generated method stub
		return null;
	}
}
