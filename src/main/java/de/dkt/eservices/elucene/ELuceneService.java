package de.dkt.eservices.elucene;

import java.io.File;

import org.apache.log4j.Logger;
import org.neo4j.cypher.internal.compiler.v2_1.perty.printToString;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.tools.ParameterChecker;
import de.dkt.eservices.elucene.indexmanagement.IndexingModule;
import de.dkt.eservices.elucene.indexmanagement.SearchingModule;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@Component
public class ELuceneService {
    
	Logger logger = Logger.getLogger(ELuceneService.class);

	public ELuceneService() {
		String storageLocation = "";
		String OS = System.getProperty("os.name");
		if(OS.startsWith("Mac")){
			storageLocation = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/";
		}
		else if(OS.startsWith("Windows")){
			storageLocation = "C:/tests/sesame/";
		}
		else if(OS.startsWith("Linux")){
			storageLocation = "/opt/storage/luceneStorage/";
		}
		IndexingModule.setIndexDirectory(storageLocation);
		SearchingModule.setIndexDirectory(storageLocation);
	}
	
	/**
	 * @param text
	 * @param languageParam
	 * @param index
	 * @return
	 * @throws ExternalServiceFailedException
	 * @throws BadRequestException
	 */
	public Model retrieveDocuments(String queryType, String text, String languageParam, String index, String indexPath, String sFields, String sAnalyzers,int hitsToReturn)//, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language", logger);
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields", logger);
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers", logger);
    	ParameterChecker.checkNotNullOrEmpty(index, "index", logger);
    	ParameterChecker.checkNotNullOrEmpty(text, "document path", logger);
        try {
        	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
        		if(!indexPath.endsWith(File.separator)){
        			indexPath += File.separator;
        		}
        		SearchingModule.setIndexDirectory(indexPath);
        	}
        	Model nifOutputModel = SearchingModule.search(index, sFields, sAnalyzers, queryType, text, languageParam, hitsToReturn);
            return nifOutputModel;
        } catch (Exception e) {
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
        }
    }

    /**
     * @param contentOrPath
     * @param languageParam
     * @param sFields
     * @param sAnalyzers
     * @param index
     * @param create
     * @return
     * @throws ExternalServiceFailedException
     * @throws BadRequestException
     */
    public Model indexDocument(Model inModel, String languageParam,String sFields,String sAnalyzers,String index,String indexPath)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language", logger);
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields", logger);
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers", logger);
    	ParameterChecker.checkNotNullOrEmpty(index, "index", logger);
    	if(inModel==null){
    		String msg = "Input model is NULL";
        	throw LoggedExceptions.generateLoggedBadRequestException(logger, msg);
    	}
    	try {
        	IndexingModule.setIndexCreate(true);
        	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
        		if(!indexPath.endsWith(File.separator)){
        			indexPath += File.separator;
        		}
        		IndexingModule.setIndexDirectory(indexPath);
        	}
			Model nifModelOutput = IndexingModule.indexModel(inModel, index, sFields, sAnalyzers, languageParam);
        	if(nifModelOutput!=null){
                return nifModelOutput;
        	}
        	else{
        		String msg = "ERROR at indexing document in index "+index;
            	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, msg);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
        }
    }

//	public String getRepositoryInformation(String repositoryName) throws ExternalServiceFailedException {
//		if(repositoryName==null || repositoryName.equals("")){
//			logger.error("No repository name given.");
//			throw new BadRequestException("No repository name given.");
//		}
//		try{
//			IndexesRepository ir = new IndexesRepository(repositoryName);
//			return ir.getListOfIndexes();
//		}
//		catch(Exception e){
//			logger.error("ERROR retrieveing information form repository "+repositoryName);
//			throw new ExternalServiceFailedException("ERROR retrieveing information form repository "+repositoryName);
//		}
//	}
//
//	public String getIndexInformation(String repositoryName, String indexName) throws ExternalServiceFailedException,BadRequestException {
//		if(repositoryName==null || repositoryName.equals("")){
//			logger.error("No repository name given.");
//			throw new BadRequestException("No repository name given.");
//		}
//		if(indexName==null || indexName.equals("")){
//			logger.error("No index name given.");
//			throw new BadRequestException("No index name given.");
//		}
//		try{
//			IndexesRepository ir = new IndexesRepository(repositoryName);
//			return ir.getIndexInformation(indexName);
//		}
//		catch(Exception e){
//			logger.error("ERROR retrieveing information form repository "+repositoryName);
//			throw new ExternalServiceFailedException("ERROR retrieveing information form repository "+repositoryName);
//		}
//	}
	
}
