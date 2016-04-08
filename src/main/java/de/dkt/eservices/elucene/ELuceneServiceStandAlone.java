package de.dkt.eservices.elucene;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.authentication.UserAuthentication;
import de.dkt.common.tools.ParameterChecker;
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
//	public String execute(
			HttpServletRequest request, 
//			HttpServletResponse response,
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
            
			@RequestParam(value = "inputType", required = false) String inputType,
			@RequestParam(value = "indexName", required = false) String index,
			@RequestParam(value = "indexPath", required = false) String indexPath,
			@RequestParam(value = "indexCreate", required = false) boolean create,
			@RequestParam(value = "keepFile", required = false) boolean keepFile,
//			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "fileType", required = false) String fileType,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "private", required = false) boolean priv,
			@RequestParam(value = "users", required = false) String sUsers,
			@RequestParam(value = "passwords", required = false) String sPasswords,
            @RequestBody(required = false) String postBody) throws Exception {

		String users[] = sUsers.split(";");
		String passwords[] = sPasswords.split(";");
		if(!create){
			//TODO Check users
			if(users.length!=1 || passwords.length!=1){
				logger.error("User and Password must have the same length (1 in case of not creating the index) [WARNING NOTE: neither user nor password can contain ';']");
				throw new IllegalAccessException("User and Password must have the same length (1 in case of not creating the index) [WARNING NOTE: neither user nor password can contain ';']");
			}
			if(!UserAuthentication.authenticateUser(users[0], passwords[0], "file", "lucene", index, false)){
				logger.error("User ["+users[0]+"] is not allow to use the index ["+index+"] ");
				throw new IllegalAccessException("User ["+users[0]+"] is not allow to use the index ["+index+"] ");
			}
		}
		
		ParameterChecker.checkNotNullOrEmpty(index, "indexName", logger);
		ParameterChecker.checkNotNullOrEmpty(inputType, "inputType", logger);
		ParameterChecker.checkInList(language, "en;de;es", "Unsupported language.", logger);
		
		String contentOrPath = "";
		if(inputType.equalsIgnoreCase("file")){
	        MultipartFile file1 = null;//= multipartRequest.getFile("file");
    		byte[] bytes;
			if (request instanceof MultipartHttpServletRequest){
		           MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		           file1 = multipartRequest.getFile("file");
		   		if(file1==null){
					logger.error("No file received in request");
					throw new BadRequestException("No file received in request");
				}
		        if (!file1.isEmpty()) {
		        	try {
		        		bytes = file1.getBytes();
		        	} catch (Exception e) {
		        		logger.error("Fail at reading input file.");
		        		throw new BadRequestException("Fail at reading input file.");
		        	}
		        } else {
		        	logger.error("The given file was empty.");
		        	throw new BadRequestException("The given file was empty.");
		        }
	        	contentOrPath = new String(bytes, "UTF-8");
	        	inputType = "string";
	        }
			else{
				ParameterChecker.checkNotNullOrEmpty(postBody, "body content",logger);
	        	contentOrPath = postBody;
	        	inputType = "string";
			}
		
//			String tmpFolder = "storage/tmp/";
//	   		//TODO store file in tmp folder.
//	   		Date dNow = new Date();
//	   		String tmpFileName = "tmpFile"+dNow.toString();
//	   		File tmpFile = FileFactory.generateOrCreateFileInstance(tmpFolder + tmpFileName);
//        	try {
//        		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tmpFile));
//        		stream.write(bytes);
//        		stream.close();
//        	} catch (Exception e) {
//        		throw new BadRequestException("Fail at uploading the file.");
//        	}
//        	contentOrPath = tmpFile.getAbsolutePath();
//        	System.out.println(contentOrPath);
//        	contentOrPath = tmpFolder+tmpFileName;
		}
		else{
			contentOrPath = postBody;
		}

		NIFParameterSet nifParameters = this.normalizeNif(contentOrPath, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);

        try {
        	Model luceneModel = service.callLuceneIndexing(
            		inputType,contentOrPath, fileType, 
            		language, sFields, sAnalyzers, 
            		index, indexPath, create);
        	
        	if(create){
        		if(users.length!=passwords.length){
        			logger.error("Users and Passwords must have the same length [WARNING NOTE: neither user nor password can contain ';']");
        			throw new BadRequestException("Users and Passwords must have the same length [WARNING NOTE: neither user nor password can contain ';']");
        		}
        		for (int j = 0; j < users.length; j++) {
            		if(!UserAuthentication.addCredentials(users[j],passwords[j], "admin", "file", "lucene",index,true)){
            			logger.error("There is a problem adding credentials for user ["+users[j]+"] in index ["+index+"]");
            			throw new ExternalServiceFailedException("There is a problem adding credentials for user ["+users[j]+"] in index ["+index+"]");
            		}
				}
        	}
    		return createSuccessResponse(luceneModel, nifParameters.getOutformat());            
        } catch (Exception e) {
        	logger.error("EXCEPTION OCCURED WITH MESSAGE: "+e.toString());
            throw e;
        }
	}

	@RequestMapping(value = "/e-lucene/retrieveDocuments", method = {
            RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> execute(
	public ResponseEntity<String> executeRetrieval(
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
			@RequestParam(value = "user", required = false) String sUser,
			@RequestParam(value = "password", required = false) String sPassword,
			@RequestParam(value = "hits", required = false) int hits,
            @RequestBody(required = false) String postBody) throws Exception {

		ParameterChecker.checkNotNullOrEmpty(indexName, "indexName", logger);
		ParameterChecker.checkNotNullOrEmpty(inputType, "inputType", logger);
		ParameterChecker.checkNotNullOrEmpty(language, "language", logger);
		ParameterChecker.checkInList(language, "en;de;es", "Unsupported language.", logger);

		if(!UserAuthentication.authenticateUser(sUser, sPassword, "file", "lucene", indexName, false)){
			logger.error("User ["+sUser+"] is not allow to use the index ["+indexName+"] ");
			throw new IllegalAccessException("User ["+sUser+"] is not allow to use the index ["+indexName+"] ");
		}

		String inputText = "";
		
		if(inputType.equalsIgnoreCase("plaintext")){
			inputText = input;
		}
		else if(inputType.equalsIgnoreCase("nif")){
			inputText = postBody;
		}
		else{
			logger.error("Input type not supported: only [plaintext/nif]");
			throw new BadRequestException("Input type not supported: only [plaintext/nif]");
		}
		
//		NIFParameterSet nifParameters = this.normalizeNif(inputText, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
        try {
            JSONObject jsonOutputModel = service.callLuceneExtraction(inputType, inputText, language, indexName, indexPath, sFields, sAnalyzers, hits);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "application/json");
            return new ResponseEntity<String>(jsonOutputModel.toString(), responseHeaders, HttpStatus.OK);
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
