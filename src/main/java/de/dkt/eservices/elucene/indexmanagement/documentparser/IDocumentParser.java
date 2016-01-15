package de.dkt.eservices.elucene.indexmanagement.documentparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * Class that generates a Lucene Document from a TXT file. The lucene document contains three fields:
 * 	- Title: the first line of the TXT file.
 *  - Body: the rest of the TXT file.
 *  - Entities: the whole text = title + body.
 */
public interface IDocumentParser {

	public Document parseDocumentFromFile (String path, String[] fields);

	public Document parseDocumentFromString (String content, String[] fields);

}