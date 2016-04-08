package de.dkt.eservices.elucene.indexmanagement.documentparser;

import org.apache.lucene.document.Document;

import com.hp.hpl.jena.rdf.model.Model;

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

	public Model parseModelFromFile (String content, String[] fields);

}