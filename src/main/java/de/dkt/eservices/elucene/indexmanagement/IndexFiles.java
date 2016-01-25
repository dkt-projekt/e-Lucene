package de.dkt.eservices.elucene.indexmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.elucene.indexmanagement.analyzer.AnalyzerFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.DocumentParserFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.IDocumentParser;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * Configures Lucene indexing files
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class IndexFiles {

	static Logger logger = Logger.getLogger(IndexFiles.class);
	
	private static Version luceneVersion = Version.LUCENE_4_9;
	
	private static String indexDirectory = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/";
	private static boolean indexCreate = false;

	private IndexFiles() {}

	/** Index a text file. */
	public static void main(String[] args) throws ExternalServiceFailedException{
		try {
			IndexFiles.index("documents/prueba.txt", "txt1", "index1", false, "all", "standard", "es");
		} catch (ExternalServiceFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Index a text file. 
	 * @param docsPath
	 * @param docType
	 * @param index
	 * @param create
	 * @param sFields
	 * @param sAnalyzers
	 * @return
	 * @throws IOException
	 * @throws ExternalServiceFailedException
	 */
	public static boolean index(String docsPath,String docType, String index,boolean create, String sFields, String sAnalyzers, String language) throws IOException,ExternalServiceFailedException{
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
		
//		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
//		ClassPathResource indexResource = new ClassPathResource(indexDirectory + index);
//		if(!indexResource.exists()){
//			logger.info("Index folder does not exist, it will be created.");
////			System.out.println("Index folder does not exist, it will be created.");
//			PathMatchingResourcePatternResolver pmr = new PathMatchingResourcePatternResolver();
//			Resource indexDirectoryResource = pmr.getResource(""+indexDirectory);
//			File f = indexDirectoryResource.getFile();
//			File f2 = new File(f, index+"/");
//			f2.mkdir();
//		}
//		File f = indexResource.getFile();
//		ctx.close();
		Directory dir = FSDirectory.open(f);

		IndexWriterConfig iwc = null;
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		String[] fields = sFields.split(";");
		String[] analyzers = sAnalyzers.split(";");
		if(fields.length!=analyzers.length){
			logger.error("The number of fields and analyzers is different");
			throw new BadRequestException("The number of fields and analyzers is different");
		}
		
		if(fields.length==1){
			Analyzer analyzer = AnalyzerFactory.getAnalyzer(analyzers[0], language, luceneVersion);
			iwc = new IndexWriterConfig(Version.LUCENE_4_9,analyzer);
		}
		else{
			/**
			 * When each field has to be analyzed with a different analyzer.
			 */
			for (int i = 0; i < fields.length; i++) {
				Analyzer particularAnalyzer = AnalyzerFactory.getAnalyzer(analyzers[i],language,luceneVersion);
				analyzerMap.put(fields[i], particularAnalyzer);
			}
			PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
					AnalyzerFactory.getAnalyzer("standard", language, luceneVersion), 
					analyzerMap);
			iwc = new IndexWriterConfig(luceneVersion,wrapper);
		}
		
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
		indexDocs(writer, docsPath, docType, fields);

		// NOTE: if you want to maximize search performance, you can optionally call forceMerge here.  This can be 
		// a terribly costly operation, so generally it's only worth it when your index is relatively static 
		// (ie you're done adding documents to it):
		// writer.forceMerge(1);
		writer.close();

		Date end = new Date();
		logger.info(end.getTime() - start.getTime() + " total milliseconds");
//		System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		return true;
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is given,
	 * recurses over files and directories found under the given directory.
	 * 
	 * @param writer Writer to the index where the given file/dir info will be stored
	 * @param docsPath The file to index, or the directory to recurse into to find files to index
	 * @throws IOException If there is a low-level I/O error
	 */
	static void indexDocs(final IndexWriter writer, String docsPath, final String doctype, String fields[]) throws IOException,ExternalServiceFailedException {
		File f = FileFactory.generateFileInstance(docsPath);
//		ClassPathResource resource = new ClassPathResource(docsPath);
//		System.out.println("SALIDA: "+resource.getPath() + "--" + resource.exists());
		final Path path = Paths.get(f.toURI());
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				indexDocs(writer, file.getName(), doctype, fields);
			}
		} else {
//			System.out.println(path.toUri().toString());
			IDocumentParser documentParser = DocumentParserFactory.getDocumentParser(doctype);
			Document doc = documentParser.parseDocumentFromFile(docsPath,fields);
			indexDoc(writer, doc, path);
		}
	}

	/** 
	 * Indexes a single document 
	 * 
	 * @param writer Index where the document is added.
	 * @param file Document to be added to the index.
	 * @param lastModified Date of last modification of the file.
	 * @throws IOException error if any low-level IO error happens.
	 */
	static void indexDoc(IndexWriter writer, Document doc,Path file) throws IOException {
		try  {
			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be there):
//				System.out.println("adding " + file);
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been indexed) so we use updateDocument instead to replace the old one matching the exact path, if present:
//				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
		catch (IOException e){
			throw e;
		}
	}

	public static String getIndexDirectory() {
		return indexDirectory;
	}

	public static void setIndexDirectory(String indexDirectory) {
		IndexFiles.indexDirectory = indexDirectory;
	}
	
	public static boolean isIndexCreate() {
		return indexCreate;
	}

	public static void setIndexCreate(boolean indexCreate) {
		IndexFiles.indexCreate = indexCreate;
	}
	
}
