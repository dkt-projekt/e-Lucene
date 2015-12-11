package eu.freme.broker.elucene.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;

import eu.freme.broker.elucene.exceptions.BadRequestException;
import eu.freme.broker.elucene.exceptions.ExternalServiceFailedException;
import eu.freme.broker.elucene.indexmanagement.documentparser.DocumentParserFactory;
import eu.freme.broker.niftools.NIF;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@RestController
public class ELuceneServiceStandAlone {

	@Autowired
	ELuceneService service;
	
	@RequestMapping(value = "/e-lucene/indexDocument", method = {
            RequestMethod.POST })
	public ResponseEntity<String> indexDocument(
//	public String execute(
			HttpServletRequest request, 
//			HttpServletResponse response, 
			@RequestParam(value = "index", required = false) String index,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "create", required = false) boolean create,
//			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "fileType", required = false) String fileType,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
            @RequestBody(required = false) String postBody) throws Exception {

        MultipartFile file1 = null;//= multipartRequest.getFile("file");
        MultipartFile file2 = null;//= multipartRequest.getFile("file2");
		if (request instanceof MultipartHttpServletRequest){
	           MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	           System.out.println("SIZE OF FILES: "+multipartRequest.getMultiFileMap().size());
	           System.out.println("SIZE OF ARAMETERS: "+multipartRequest.getParameterMap().size());
	           file1 = multipartRequest.getFile("file");
	           file2 = multipartRequest.getFile("file2");
	   		if(file1==null){
				System.out.println("FILE1 is null");
			}
			else{
				System.out.println("FILE1:"+file1.getName());
			}
			if(file2==null){
				System.out.println("FILE2 is null");
			}
			else{
				System.out.println("FILE2:"+file2.getName());
			}
        }
		
		System.out.println("BODY:" + postBody);
        // Check the language parameter.
        if(language == null) {
            throw new BadRequestException("Parameter language is not specified");
        } else {
            if(language.equals("en") 
                    || language.equals("de") 
                    || language.equals("es")
                    ) {
                // OK, the language is supported.
            } else {
                // The language specified with the language parameter is not supported.
                throw new BadRequestException("Unsupported language.");
            }
        }

        if(index == null && i==null) {
        	throw new BadRequestException("Unspecified index name.");            
        }
        else{
        	if(index==null){
        		index = i;
        	}
        }
        
        String documentsDirectory = "documents/";
        String docpath = documentsDirectory + fileName;
        ClassPathResource documentResource = new ClassPathResource(docpath);
        File f2;
		if(!documentResource.exists()){
			f2 = new File(new ClassPathResource(documentsDirectory).getFile(), fileName);
			f2.createNewFile();
		}
		else{
			throw new BadRequestException("A file with the same name has been indexed previously. If you still want to index it, please provide a different name.");
		}

        if (!file1.isEmpty()) {
        	try {
        		byte[] bytes = file1.getBytes();
        		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f2));
        		stream.write(bytes);
        		stream.close();
        	} catch (Exception e) {
        		throw new BadRequestException("Fail at uploading the file.");
        	}
        } else {
        	throw new BadRequestException("The given file was empty.");
        }

        try {
            ResponseEntity<String> lucenes = service.callLuceneIndexing("file",f2.getAbsolutePath(), fileType, language, sFields, sAnalyzers, index, create);
            
            //TODO We should return a NIF structure with the initial description of the document.

            Model outModel = ModelFactory.createDefaultModel();
//            outModel.setNsPrefixes(NIFTransferPrefixMapping.getInstance());
            Map<String,String> prefixes = new HashMap<String, String>();
            prefixes.put("xsd", "<http://www.w3.org/2001/XMLSchema#>");
            prefixes.put("nif", "<http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#>");
            prefixes.put("dfkinif", "<http://persistence.dfki.de/nif/ontologies/nif-dfki#>");
            outModel.setNsPrefixes(prefixes);
            
            String documentURI = "http://lucene.dfki.dkt.de/"+fileName;

            String inputText = DocumentParserFactory.getDocumentParser(fileType).parseDocumentFromFile(f2.getAbsolutePath(),sFields.split(";")).get("content");
            int start = 0;
            int end = inputText.codePointCount(0, inputText.length());
            
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append(documentURI)
            		.append("#char=")
            		.append(start)
            		.append(',')
            		.append(end);
            String documentUri = uriBuilder.toString();

            com.hp.hpl.jena.rdf.model.Resource modelDocumentResource = outModel.createResource(documentUri);
            outModel.add(modelDocumentResource, RDF.type, NIF.Context);
            outModel.add(modelDocumentResource, RDF.type, NIF.String);
            outModel.add(modelDocumentResource, RDF.type, NIF.RFC5147String);
            // TODO add language to String
            outModel.add(modelDocumentResource, NIF.isString,
                    outModel.createTypedLiteral(inputText, XSDDatatype.XSDstring));
            outModel.add(modelDocumentResource, NIF.beginIndex,
                    outModel.createTypedLiteral(0, XSDDatatype.XSDnonNegativeInteger));
            outModel.add(modelDocumentResource, NIF.endIndex,
                    outModel.createTypedLiteral(end, XSDDatatype.XSDnonNegativeInteger));

            outModel.add(modelDocumentResource, ResourceFactory.createProperty(prefixes.get("dfkinif"),""),
                    outModel.createTypedLiteral("luceneDocument", XSDDatatype.XSDstring));

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "RDF/XML");
    		StringWriter writer = new StringWriter();
    		outModel.write(writer, "RDF/XML");
    		try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		String rdfString = writer.toString();

           	return new ResponseEntity<String>(rdfString, responseHeaders, HttpStatus.OK);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ExternalServiceFailedException e) {
            throw e;
        }
	}

	@RequestMapping(value = "/e-lucene/retrieveDocuments", method = {
            RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> execute(
	public String executeRetrieval(
			@RequestParam(value = "index", required = false) String index,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "t", required = false) String t,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "fields", required = false) String sFields,
			@RequestParam(value = "analyzers", required = false) String sAnalyzers,
			@RequestParam(value = "int", required = false) int hits,
            @RequestBody(required = false) String postBody) throws Exception {

        // Check the language parameter.
        if(language == null) {
            throw new BadRequestException("Parameter language is not specified");
        } else {
            if(language.equals("en") 
                    || language.equals("de") 
//                    || language.equals("nl")
//                    || language.equals("it")
//                    || language.equals("fr")
                    || language.equals("es")
//                    || language.equals("ru")
                   ) {
                // OK, the language is supported.
            } else {
                // The language specified with the language parameter is not supported.
                throw new BadRequestException("Unsupported language.");
            }
        }

        if(index == null && i==null) {
        	throw new BadRequestException("Unspecified index name.");            
        }
        else{
        	if(index==null){
        		index = i;
        	}
        }
        // Check the text parameter.
        if(text == null) {
        	if(t==null){
        		throw new BadRequestException("Unspecified text query.");
        	}
        	else{
        		text = t;
        	}
        }

        try {
            ResponseEntity<String> lucenes = service.callLuceneExtraction(text, language, index, sFields, sAnalyzers, hits);
            return lucenes.getBody();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ExternalServiceFailedException e) {
            throw e;
        }
	}

	// Get info about a specific index.
	// curl -v "http://localhost:8080/e-lucene/repositoryInfo
	@RequestMapping(value = "/e-lucene/repositoryInfo", method = {
			RequestMethod.GET })
	public String getRepositoryInformation(
			@RequestParam(value = "repoName") String repoName) throws ExternalServiceFailedException {
		// Check the dataset name parameter.
		if(repoName == null) {
			throw new BadRequestException("Unspecified repository name.");            
		}
		try{
			String result = service.getRepositoryInformation(repoName);
			return result;
		}
		catch(Exception e){
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	// Get info about a specific index.
	// curl -v "http://localhost:8080/e-lucene/indexInfo
	@RequestMapping(value = "/e-lucene/indexInfo", method = {
			RequestMethod.GET })
	public String getIndexInformation(
			@RequestParam(value = "repoName") String repoName,
			@RequestParam(value = "indexName") String indexName) throws ExternalServiceFailedException {
		// Check the dataset name parameter.
		if(repoName == null) {
			throw new BadRequestException("Unspecified repository name.");            
		}
		if(indexName == null) {
			throw new BadRequestException("Unspecified index name.");            
		}
		try{
			String result = service.getIndexInformation(repoName,indexName);
			return result;
		}
		catch(Exception e){
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	
//	// Retrieving the list of all available indexes.
//	// curl -v "http://localhost:8080/e-lucene/indexes/list"
//	@RequestMapping(value = "/e-lucene/indexes/list", method = {
//			RequestMethod.GET })
//	public String getAllIndexes(
//					@RequestParam(value = "repository", required = false) String repository) throws ExternalServiceFailedException {
//		try{
//			IndexesRepository ir = new IndexesRepository(repository);
//			String result = ir.getListOfIndexes();
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
