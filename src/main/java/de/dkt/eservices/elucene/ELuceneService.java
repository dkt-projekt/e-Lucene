package de.dkt.eservices.elucene;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.elucene.indexmanagement.LuceneModule;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;
import eu.freme.common.persistence.dao.IndexDAO;
import eu.freme.common.persistence.repository.IndexRepository;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@Component
public class ELuceneService {
    
	Logger logger = Logger.getLogger(ELuceneService.class);

	LuceneModule luceneModule;

	@Autowired
	private IndexRepository indexRepository;

	@Autowired
	private IndexDAO indexDAO;

//	@Value("${luceneIndexPath}")
	@Value("${dkt.storage.data-dir}/lucene")
	private String luceneIndexPath;

	public ELuceneService() {
	}

	@PostConstruct
	public void createLuceneModule(){
		luceneModule = LuceneModule.getInstance(luceneIndexPath,indexRepository,indexDAO);
	}

	public String createIndex(String indexId, String language,String sFields,String sAnalyzers, boolean overwrite) throws Exception {
		try {
			String nifResult = luceneModule.createIndex(indexId, language, sFields, sAnalyzers, overwrite);
			return nifResult;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public String deleteIndex(String indexId) throws ExternalServiceFailedException, BadRequestException {
		try {
			String nifResult = luceneModule.deleteIndex(indexId);
			return nifResult;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public void deleteDocument(String indexId, String documentId) throws Exception {
		try {
			luceneModule.deleteDocument(documentId, indexId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public Model deleteAndRetrieveDocument(String indexId, String documentId) throws Exception {
		try {
			Model model = luceneModule.deleteAndRetrieveDocument(documentId, indexId);
			return model;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public String listIndexes() throws ExternalServiceFailedException, BadRequestException {
		try {
			String nifResult = luceneModule.listIndexes();
			return nifResult;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
			throw e;
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
    public Model addDocument(Model inModel, String index)
            throws ExternalServiceFailedException, BadRequestException {
    	if(inModel==null){
    		String msg = "Input model is NULL";
        	throw LoggedExceptions.generateLoggedBadRequestException(logger, msg);
    	}
    	try {
			Model nifModelOutput = luceneModule.addDocument(inModel, index);
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

	/**
	 * @param text
	 * @param languageParam
	 * @param index
	 * @return
	 * @throws ExternalServiceFailedException
	 * @throws BadRequestException
	 */
	public Model retrieveDocuments(String indexId, String queryText, int hitsToReturn) throws Exception {
        try {
        	Model nifOutputModel = luceneModule.search(indexId, queryText, hitsToReturn);
            return nifOutputModel;
        } catch (Exception e) {
        	throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
        }
    }

	
}
