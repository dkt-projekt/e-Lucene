package de.dkt.eservices.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a TXT file. The lucene document contains three fields:
 * 	- Title: the first line of the TXT file.
 *  - Body: the rest of the TXT file.
 *  - Entities: the whole text = title + body.
 *  - Path: the name of the file.
 */
public class TXTDocumentParser implements IDocumentParser{

	public TXTDocumentParser() {
	}
	
	@Override
	public Document parseDocumentFromFile(String path, String[] fields) {
		try{
			//For spring approach the next lines must be used
			//ClassPathResource cpr = new ClassPathResource("classpath:"+path);
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cpr.getFile()), "utf-8"));
			Document doc = new Document();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String title = br.readLine();
			String line = br.readLine();
			String body = null;
			while(line!=null){
				if(body==null)
					body = line;
				else
					body=body + " " + line;
				line = br.readLine();
			}
			br.close();
			doc.add(new TextField("title", title, Field.Store.YES));
			doc.add(new TextField("body", body, Field.Store.YES));
			doc.add(new TextField("entities", title + " " + body, Field.Store.NO));
			doc.add(new StringField("path", path, Field.Store.YES));

			// Note that FileReader expects the file to be in UTF-8 encoding. If that's not the case searching for special characters will fail.
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
