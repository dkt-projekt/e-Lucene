package de.dkt.eservices.elucene;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
import de.dkt.common.tools.ResponseGenerator;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;
import eu.freme.common.persistence.dao.IndexDAO;
import eu.freme.common.persistence.repository.IndexRepository;
import eu.freme.common.rest.BaseRestController;
import eu.freme.common.rest.NIFParameterSet;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@RestController
public class ELuceneRestController extends BaseRestController {

	Logger logger = Logger.getLogger(ELuceneRestController.class);

	@Autowired
	ELuceneService service;
	
	@Autowired
	IndexRepository indexRepository;
	
	@Autowired
	IndexDAO indexDAO;
	
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

	@RequestMapping(value = "/e-lucene/indexes", method = { RequestMethod.GET })
	public ResponseEntity<String> listIndexes(
			HttpServletRequest request, 
            @RequestBody(required = false) String postBody) throws Exception {
		try{
        	String jsonResult = service.listIndexes();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/listIndexes", "Success", "", "Exception", "", "");
        	return ResponseGenerator.successResponse(jsonResult, "application/json");            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/listIndexes", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes", method = { RequestMethod.POST })
	public ResponseEntity<String> createIndexPOST(
			HttpServletRequest request, 
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestParam(value = "indexName", required = false) String indexName,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "overwrite", required = false) boolean overwrite,
            @RequestBody(required = false) String postBody) throws Exception {

        try {
        	
        	//TODO what to do with the index ID.
        	
        	String result = service.createIndex(indexName, language, sFields, sAnalyzers, overwrite);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/createIndex", "Success", "", "Exception", "", "");
    		return ResponseGenerator.successResponse(result, "text/plain");            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/createIndex", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes/{indexId}", method = { RequestMethod.PUT })
	public ResponseEntity<String> createIndexPUT(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "overwrite", required = false) boolean overwrite,
            @RequestBody(required = false) String postBody) throws Exception {

        try {
        	String result = service.createIndex(indexId, language, sFields, sAnalyzers, overwrite);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/createIndex", "Success", "", "Exception", "", "");
    		return ResponseGenerator.successResponse(result, "text/plain");            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/createIndex", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes/{indexId}", method = { RequestMethod.DELETE })
	public ResponseEntity<String> deleteIndex(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "overwrite", required = false) boolean overwrite,
            @RequestBody(required = false) String postBody) throws Exception {

        try {
        	String result = service.deleteIndex(indexId);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/deleteIndex", "Success", "", "Exception", "", "");
    		return ResponseGenerator.successResponse(result, "text/plain");            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/deleteIndex", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes/{indexId}", method = { RequestMethod.POST })
	public ResponseEntity<String> addDocument(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestParam(value = "language", required = false) String language,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
            @RequestBody(required = false) String postBody) throws Exception {

		if(input==null || input.equalsIgnoreCase("")){
			input=postBody;
			if(input==null || input.equalsIgnoreCase("")){
				throw LoggedExceptions.generateLoggedBadRequestException(logger, "No document content provided");
			}
		}
        NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
        Model inModel = null;
        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
			rdfConversionService.plaintextToRDF(inModel, nifParameters.getInput(),language, nifParameters.getPrefix());
        } else {
            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
        }
        if(inModel==null){
        	String msg = "The NIF input model is NULL";
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/addDocument", msg, "", "Exception", msg, "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, msg);
        }
        try {
        	Model luceneModel = service.addDocument(inModel, indexId);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/addDocument", "Success", "", "Exception", "", "");
    		return createSuccessResponse(luceneModel, nifParameters.getOutformat());            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/addDocument", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}
	
	@RequestMapping(value = "/e-lucene/indexes/{indexId}/{documentId}", method = { RequestMethod.PUT })
	public ResponseEntity<String> updateDocument(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@PathVariable String documentId,
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestParam(value = "language", required = false) String language,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
            @RequestBody(required = false) String postBody) throws Exception {

		
		//TODO ------
		
		if(input==null || input.equalsIgnoreCase("")){
			input=postBody;
			if(input==null || input.equalsIgnoreCase("")){
				throw LoggedExceptions.generateLoggedBadRequestException(logger, "No document content provided");
			}
		}
        NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
        Model inModel = null;
        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
			rdfConversionService.plaintextToRDF(inModel, nifParameters.getInput(),language, nifParameters.getPrefix());
        } else {
            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
        }
        if(inModel==null){
        	String msg = "The NIF input model is NULL";
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/addDocument", msg, "", "Exception", msg, "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, msg);
        }
        try {
        	Model luceneModel = service.addDocument(inModel, indexId);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/addDocument", "Success", "", "Exception", "", "");
    		return createSuccessResponse(luceneModel, nifParameters.getOutformat());            
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/addDocument", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes/{indexId}/{documentId}", method = { RequestMethod.DELETE })
	public ResponseEntity<String> deleteDocument(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@PathVariable String documentId,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
            @RequestBody(required = false) String postBody) throws Exception {
        try {
        	System.out.println("DOCID: "+documentId);
        	service.deleteDocument(indexId, documentId);
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/deleteDocument", "Success", "", "Exception", "", "");

	    	ResponseEntity<String> response = new ResponseEntity<String>("The document has been deleted correctly.", new HttpHeaders(), HttpStatus.OK);
    		return response;
        } catch (Exception e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/deleteDocument", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        }
	}

	@RequestMapping(value = "/e-lucene/indexes/{indexId}/{documentId}", method = { RequestMethod.GET })
	public ResponseEntity<String> retrieveDocument(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@PathVariable String documentId,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestParam(value = "hits", required = false) int hits,
            @RequestBody(required = false) String postBody) throws Exception {
        try {
        	
        	//TODO retrieve only a concrete document.
        	
            Model outputModel = service.retrieveDocuments(indexId, query, hits);
            HttpHeaders responseHeaders = new HttpHeaders();
            String output = rdfConversionService.serializeRDF(outputModel, RDFSerialization.fromValue(outformat));
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/retrieveDocuments", "Success", "", "Exception", "", "");
            return new ResponseEntity<String>(output, responseHeaders, HttpStatus.OK);
        } catch (BadRequestException e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/retrieveDocuments", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        } catch (ExternalServiceFailedException e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/retrieveDocuments", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
        }
	}
	
	@RequestMapping(value = "/e-lucene/indexes/{indexId}/search", method = { RequestMethod.GET })
	public ResponseEntity<String> retrieveDocuments(
			HttpServletRequest request, 
			@PathVariable String indexId,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestParam(value = "hits", required = false) int hits,
            @RequestBody(required = false) String postBody) throws Exception {
        try {
            Model outputModel = service.retrieveDocuments(indexId, query, hits);
            HttpHeaders responseHeaders = new HttpHeaders();
            String output = rdfConversionService.serializeRDF(outputModel, RDFSerialization.fromValue(outformat));
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "usage", "e-Lucene/retrieveDocuments", "Success", "", "Exception", "", "");
            return new ResponseEntity<String>(output, responseHeaders, HttpStatus.OK);
        } catch (BadRequestException e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/retrieveDocuments", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedBadRequestException(logger, e.getMessage());
        } catch (ExternalServiceFailedException e) {
        	e.printStackTrace();
			InteractionManagement.sendInteraction("dkt-usage@"+request.getRemoteAddr(), "error", "e-Lucene/retrieveDocuments", e.getMessage(), "", "Exception", e.getMessage(), "");
			throw LoggedExceptions.generateLoggedExternalServiceFailedException(logger, e.getMessage());
        }
	}
	
}
