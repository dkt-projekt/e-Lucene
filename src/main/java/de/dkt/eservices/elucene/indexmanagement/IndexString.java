package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.common.niftools.NIFReader;
import de.dkt.common.niftools.NIFWriter;
import de.dkt.eservices.elucene.indexmanagement.analyzer.AnalyzerFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.DocumentParserFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.IDocumentParser;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * Configures Lucene indexing files
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class IndexString {

	static Logger logger = Logger.getLogger(IndexString.class);
	
	private static Version luceneVersion = Version.LUCENE_3_0;
	
	private static String indexDirectory  ="/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/";
	private static boolean indexCreate = false;

	private IndexString() {}

	/** Index a text file. */
	public static void main(String[] args) throws ExternalServiceFailedException{
		try {
			String inputText = "@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."+
					"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> ."+
					"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> ."+
					"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> ."+
					"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> ."+
					""+
					"<http://dkt.dfki.de/examples/#char=0,813>"+
					"        a                  nif:RFC5147String , nif:String , nif:Context ;"+
					"        nif:beginIndex     \"0\"^^xsd:nonNegativeInteger ;"+
					"        nif:endIndex       \"813\"^^xsd:nonNegativeInteger ;"+
					"        nif:isString       \"\"\"1936\n\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n\"\"\"^^xsd:string ;"+
					"        nif:meanDateRange  \"02110711030000_16631213030000\"^^xsd:string .";
			
			IndexString.setIndexDirectory("/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/luceneindexes/");
//			IndexString.index("This is a testing document that will be used for probing "
//					+ "#the lucene indexation capabilities and module.",
//					"nif", "test3", true, "content", "standard", "en");
			IndexString.index(inputText,
					"nif", "test3", true, "content", "standard", "en");
		} catch (ExternalServiceFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Index a text file. 
	 * @param docContent
	 * @param docType
	 * @param index
	 * @param create
	 * @param sFields
	 * @param sAnalyzers
	 * @return
	 * @throws IOException
	 * @throws ExternalServiceFailedException
	 */
	public static String index(String docContent,String docType, String index,boolean create, String sFields, String sAnalyzers, String language) throws IOException,ExternalServiceFailedException{
		Date start = new Date();
		logger.info("Indexing to directory '" + indexDirectory + index + "'...");
//		System.out.println("Indexing to directory '" + indexDirectory + index + "'...");

		File f;
		if(create){
			f = FileFactory.generateOrCreateDirectoryInstance(indexDirectory + index);
		}
		else{
			f = FileFactory.generateFileInstance(indexDirectory + index);
		}		
		Directory dir = FSDirectory.open(f);

		IndexWriterConfig iwc = null;
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		String[] fields = sFields.split(";");
		String[] analyzers = sAnalyzers.split(";");
		if(fields.length!=analyzers.length){
			logger.error("The number of fields and analyzers is different");
			throw new BadRequestException("The number of fields and analyzers is different");
		}
		
//		if(fields.length==1){
//			Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], language, luceneVersion);
//System.out.println("ANALYZER: "+analyzer.getClass());
//			iwc = new IndexWriterConfig(luceneVersion,analyzer);
//		}
//		else{
//			/**
//			 * When each field has to be analyzed with a different analyzer.
//			 */
//			for (int i = 0; i < fields.length; i++) {
//				Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language,luceneVersion);
//				analyzerMap.put(fields[i], particularAnalyzer);
//			}
//			PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
//					AnalyzerFactory.getAnalyzer("standard", language, luceneVersion), 
//					analyzerMap);
//			iwc = new IndexWriterConfig(luceneVersion,wrapper);
//		}

		for (int i = 0; i < fields.length; i++) {
			System.out.println("GENERATING "+analyzers[i]+" for "+fields[i]);
			Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language,luceneVersion);
			analyzerMap.put(fields[i], particularAnalyzer);
		}
		PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
				AnalyzerFactory.getAnalyzer("standard", language, luceneVersion), 
				analyzerMap);
		iwc = new IndexWriterConfig(luceneVersion,wrapper);

		if (create) {
			// Create a new index in the directory, removing any previously indexed documents:
			iwc.setOpenMode(OpenMode.CREATE);
		} else {
			// Add new documents to an existing index:
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		// Optional: for better indexing performance, if you are indexing many documents, increase the RAM
		// buffer.  But if you do this, increase the max heap size to the JVM (eg add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);

		IndexWriter writer = new IndexWriter(dir, iwc);
		
//		System.out.println("CONTENT TO INDEX" + docContent);
		
		IDocumentParser documentParser = DocumentParserFactory.getDocumentParser(docType);
		Document doc = documentParser.parseDocumentFromString(docContent,fields);

//		System.out.println("Document to index:" + doc);
		
		try  {
			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be there):
//				System.out.println("adding " + file);
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been indexed) so we use updateDocument instead to replace the old one matching the exact path, if present:
//				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", doc.get("path")), doc);
			}
		}
		catch (IOException e){
			e.printStackTrace();
			writer.close();
			throw e;
		}
//		finally{
//			// NOTE: if you want to maximize search performance, you can optionally call forceMerge here.  This can be 
//			// a terribly costly operation, so generally it's only worth it when your index is relatively static 
//			// (ie you're done adding documents to it):
//			// writer.forceMerge(1);
//			writer.close();
//		}
		writer.commit();
		int numDocs = writer.numDocs();
		System.out.println("NUM OF DOCS:" + numDocs);
		writer.close();

		Date end = new Date();
		logger.info(end.getTime() - start.getTime() + " total milliseconds");

		
		
//		dir = FSDirectory.open(f);
//		IndexReader reader = DirectoryReader.open(dir);
//		
//		System.out.println(reader.numDocs());
//		
//		System.out.println(reader.document(0));
//		IndexSearcher searcher = new IndexSearcher(reader);

//		return "Well indexed: "+numDocs;
		try{
			Model model = null;
			try{
				model = NIFReader.extractModelFromFormatString(docContent,RDFSerialization.RDF_XML);
			}
			catch(Exception e){
				model = NIFReader.extractModelFromFormatString(docContent,RDFSerialization.TURTLE);
			}
			String textToProcess = NIFReader.extractIsString(model);
			NIFWriter.addLuceneIndexingInformation(model, textToProcess, "http://dkt.dfki.de/examples/", index, indexDirectory);
			String nifOutputString = NIFReader.model2String(model, "TTL");
			return nifOutputString;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException("Error at generating LUCENE output into NIF string.");
		}
	}

	public static String getIndexDirectory() {
		return indexDirectory;
	}

	public static void setIndexDirectory(String indexDirectory) {
		IndexString.indexDirectory = indexDirectory;
	}


	public static boolean isIndexCreate() {
		return indexCreate;
	}

	public static void setIndexCreate(boolean indexCreate) {
		IndexString.indexCreate = indexCreate;
	}

	
	
}
