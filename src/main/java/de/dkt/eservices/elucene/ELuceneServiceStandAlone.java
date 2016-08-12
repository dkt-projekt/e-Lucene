package de.dkt.eservices.elucene;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.exceptions.LoggedExceptions;
import de.dkt.common.feedback.InteractionManagement;
import de.dkt.common.niftools.NIFReader;
import de.dkt.common.tools.ParameterChecker;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;
import eu.freme.common.rest.BaseRestController;
import eu.freme.common.rest.NIFParameterSet;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@RestController
public class ELuceneServiceStandAlone extends BaseRestController {

	Logger logger = Logger.getLogger(ELuceneServiceStandAlone.class);

	@Autowired
	ELuceneService service;
	
	@Autowired
	RDFConversionService rdfConversionService;

	@RequestMapping(value = "/e-lucene/testURL", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> testURL(
			@RequestParam(value = "preffix", required = false) String preffix,
            @RequestBody(required = false) String postBody) throws Exception {

    	HttpHeaders responseHeaders = new HttpHeaders();
    	responseHeaders.add("Content-Type", "text/plain");
    	ResponseEntity<String> response = new ResponseEntity<String>("The restcontroller is working properly", responseHeaders, HttpStatus.OK);
    	return response;
	}
	
	@RequestMapping(value = "/e-lucene/indexDocument", method = { RequestMethod.POST })
	public ResponseEntity<String> indexDocument(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
            @RequestParam Map<String, String> allParams,
            
			@RequestParam(value = "indexName", required = false) String index,
			@RequestParam(value = "indexPath", required = false) String indexPath,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
            @RequestBody(required = false) String postBody) throws Exception {

		ParameterChecker.checkNotNullOrEmpty(index, "indexName", logger);
		ParameterChecker.checkNotNullOrEmpty(informat, "informat", logger);
		ParameterChecker.checkInList(language, "en;de;es", "Unsupported language.", logger);
		
        NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
        Model inModel = null;
        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
			rdfConversionService.plaintextToRDF(inModel, nifParameters.getInput(),language, nifParameters.getPrefix());
        } else {
            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
        }
        if(inModel==null){
        	String msg = "The NIF input model is NULL";
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/indexDocument", msg, "", "Exception", msg, "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, msg);
        }
        try {
        	Model luceneModel = service.indexDocument(inModel, language, sFields, sAnalyzers, index, indexPath);
    		return createSuccessResponse(luceneModel, nifParameters.getOutformat());            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/indexDocument", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/retrieveDocuments", method = {
            RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> execute(
	public ResponseEntity<String> executeRetrieval(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
            @RequestParam Map<String, String> allParams,
            
            @RequestParam(value = "indexName", required = false) String indexName,
            @RequestParam(value = "indexPath", required = false) String indexPath,
			@RequestParam(value = "inputType", required = false) String inputType,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "hits", required = false) int hits,
            @RequestBody(required = false) String postBody) throws Exception {

		ParameterChecker.checkNotNullOrEmpty(indexName, "indexName", logger);
		ParameterChecker.checkNotNullOrEmpty(language, "language", logger);
		ParameterChecker.checkInList(language, "en;de;es", "Unsupported language.", logger);
		if (input == null) {
			input = i;
		}
		if (informat == null) {
			informat = f;
		}
		if (outformat == null) {
			outformat = o;
		}
		if (prefix == null) {
			prefix = p;
		}
		String query = null;
		NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
        Model inModel = null;
        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
        	query = nifParameters.getInput();
        } else {
            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
        	query = NIFReader.extractIsString(inModel);
            if (query == null) {
    			String msg = "No query to process. Input type not supported: only [plaintext/nif]";
    			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Sesame/retrieveData", "Input data is not in the proper format ...", "", "Exception", msg, "");
    			throw new BadRequestException(msg);
            }
        }
        try {
            Model outputModel = service.retrieveDocuments(inputType, query, language, indexName, indexPath, sFields, sAnalyzers, hits);
            HttpHeaders responseHeaders = new HttpHeaders();
            String output = rdfConversionService.serializeRDF(outputModel, nifParameters.getOutformat());
            return new ResponseEntity<String>(output, responseHeaders, HttpStatus.OK);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (ExternalServiceFailedException e) {
            logger.error(e.getMessage());
            throw e;
        }
	}

//	// Get info about a specific index.
//	// curl -v "http://localhost:8080/e-lucene/repositoryInfo
//	@RequestMapping(value = "/e-lucene/repositoryInfo", method = {
//			RequestMethod.GET })
//	public String getRepositoryInformation(
//			@RequestParam(value = "repoName") String repoName) throws ExternalServiceFailedException {
//		// Check the dataset name parameter.
//		if(repoName == null) {
//			throw new BadRequestException("Unspecified repository name.");            
//		}
//		try{
//			String result = service.getRepositoryInformation(repoName);
//			return result;
//		}
//		catch(Exception e){
//			throw new ExternalServiceFailedException(e.getMessage());
//		}
//	}

//	// Get info about a specific index.
//	// curl -v "http://localhost:8080/e-lucene/indexInfo
//	@RequestMapping(value = "/e-lucene/indexInfo", method = {
//			RequestMethod.GET })
//	public String getIndexInformation(
//			@RequestParam(value = "repoName") String repoName,
//			@RequestParam(value = "indexName") String indexName) throws ExternalServiceFailedException {
//		// Check the dataset name parameter.
//		if(repoName == null) {
//			throw new BadRequestException("Unspecified repository name.");            
//		}
//		if(indexName == null) {
//			throw new BadRequestException("Unspecified index name.");            
//		}
//		try{
//			String result = service.getIndexInformation(repoName,indexName);
//			return result;
//		}
//		catch(Exception e){
//			throw new ExternalServiceFailedException(e.getMessage());
//		}
//	}
	
//	// Removing a specific document from an index.
//	// curl -v "http://localhost:8080/e-lucene/deleteDoc" -X DELETE
//	@RequestMapping(value = "/e-lucene/deleteDoc/", method = {
//			RequestMethod.DELETE })
////	public ResponseEntity<String> removeDataset(
//	public String removeDataset(
//			@RequestParam(value = "index", required = false) String index,
//			@RequestParam(value = "i", required = false) String i,
//			@RequestParam(value = "field", required = false) String field,
//			@RequestParam(value = "f", required = false) String f,
//			@RequestParam(value = "text", required = false) String text,
//			@RequestParam(value = "t", required = false) String t,
////			@RequestParam(value = "language", required = false) String language,
//            @RequestBody(required = false) String postBody) throws Exception {
//
//		// Check the index name parameter.
//		if(index == null && i==null) {
//			throw new BadRequestException("Unspecified index name.");            
//		}
//		else{
//			if(index==null){
//				index = i;
//			}
//		}
//		// Check the field parameter.
//		if(field == null) {
//			if(f==null){
//				throw new BadRequestException("Unspecified field name.");
//			}
//			else{
//				field = f;
//			}
//		}
//		// Check the text parameter.
//		if(text == null) {
//			if(t==null){
//				throw new BadRequestException("Unspecified text name.");
//			}
//			else{
//				text = t;
//			}
//		}
//		//Everything is ok, now remove the document from the index.
//		if(service.callLuceneRemover(index,field,text)){
//			return "File succesfully removed from index ["+index+"]";
//		}
//		else{
//			throw new ExternalServiceFailedException("This document (field-identifier) pair is not contained in the specified index.");
//		}
//	}
//
}
