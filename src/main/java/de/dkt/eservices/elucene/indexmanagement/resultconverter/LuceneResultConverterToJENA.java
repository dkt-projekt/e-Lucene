package de.dkt.eservices.elucene.indexmanagement.resultconverter;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.niftools.NIFManagement;
import de.dkt.common.niftools.NIFReader;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.ExternalServiceFailedException;

public class LuceneResultConverterToJENA {
	
	static Logger logger = Logger.getLogger(LuceneResultConverterToJENA.class);
	
	public static Model convertResults(Query query,IndexSearcher searcher,TopDocs results) throws ExternalServiceFailedException{
		try{
			ScoreDoc[] hits = results.scoreDocs;
			int numTotalHits = hits.length;//results.totalHits;
			System.out.println(numTotalHits);
			Date d = new Date();
			String prefix = "http://dkt.dfki.de/collection/retrievedDocuments/"+d.getTime();
			Model collectionModel = NIFManagement.createDefaultCollectionModel(prefix);

			//TODO Should we put the query into the prefix????
			for (int i = 0; i < numTotalHits; i++) {
				Document doc = searcher.doc(hits[i].doc);
				String nifContent = doc.get("nifcontent"); 
//				System.out.println("DEBUG: nifcontent: "+nifContent);
				Model docModel = NIFReader.extractModelFromFormatString(nifContent, RDFSerialization.TURTLE);
				NIFManagement.addDocumentToCollection(collectionModel, docModel);
			}
			return collectionModel;
		}
		catch(Exception e){
			e.printStackTrace();
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, "Error at converting LUCENE output to JENA:: " + e.getMessage());
		}
	}

}
