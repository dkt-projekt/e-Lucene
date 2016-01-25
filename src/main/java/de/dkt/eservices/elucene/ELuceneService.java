package de.dkt.eservices.elucene;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.dkt.common.tools.ParameterChecker;
import de.dkt.common.tools.ResponseGenerator;
import de.dkt.eservices.elucene.indexmanagement.IndexFiles;
import de.dkt.eservices.elucene.indexmanagement.IndexString;
import de.dkt.eservices.elucene.indexmanagement.IndexesRepository;
import de.dkt.eservices.elucene.indexmanagement.SearchFiles;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@Component
public class ELuceneService {
    
	/**
	 * @param text
	 * @param languageParam
	 * @param index
	 * @return
	 * @throws ExternalServiceFailedException
	 * @throws BadRequestException
	 */
	public ResponseEntity<String> callLuceneExtraction(String queryType, String text, String languageParam, String index, String indexPath, String sFields, String sAnalyzers,int hitsToReturn)//, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language");
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields");
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers");
    	ParameterChecker.checkNotNullOrEmpty(index, "index");
    	ParameterChecker.checkNotNullOrEmpty(text, "document path");
        try {
//            System.out.println(text);
//            System.out.println(URLDecoder.decode(text, "UTF-8"));
//            String nif = "Test for the service to be working";
        	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
        		if(!indexPath.endsWith(File.separator)){
        			indexPath += File.separator;
        		}
        		SearchFiles.setIndexDirectory(indexPath);
        	}
        	String nif = SearchFiles.search(index, sFields, sAnalyzers, queryType, text, languageParam, hitsToReturn);
//            String nif = "We will return the document: " + text + " in the language: " + languageParam;
            return ResponseGenerator.successResponse(nif, "RDF/XML");
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ExternalServiceFailedException(e.getMessage());
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
    public ResponseEntity<String> callLuceneIndexing(String inputType, String contentOrPath, 
    		String docType, String languageParam,String sFields,String sAnalyzers,
    		String index,String indexPath,boolean create)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(inputType, "inputType [should be file/string] ");
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language");
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields");
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers");
    	ParameterChecker.checkNotNullOrEmpty(index, "index");
    	ParameterChecker.checkNotNullOrEmpty(contentOrPath, "document path");
//    	System.out.println(inputType);
//    	System.out.println(languageParam);
//    	System.out.println(sFields);
//    	System.out.println(sAnalyzers);
//    	System.out.println(index);
//    	System.out.println(path);
//    	System.out.println(create);
//    	System.out.println();
    	try {
    		if(inputType.equalsIgnoreCase("file")){
    			
            	IndexFiles.setIndexCreate(create);
            	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
            		if(!indexPath.endsWith(File.separator)){
            			indexPath += File.separator;
            		}
            		IndexFiles.setIndexDirectory(indexPath);
            	}
    			
            	if(IndexFiles.index(contentOrPath, docType, index, create, sFields, sAnalyzers, languageParam)){
            		String nif = "Document: " + contentOrPath + " in language: " + languageParam + "has been correctly indexed in index: "+index;
                    return ResponseGenerator.successResponse(nif, "RDF/XML");
            	}
            	else{
            		throw new ExternalServiceFailedException("ERROR at indexing document "+contentOrPath+" in index "+index);
            	}
    		}
    		else{
            	IndexString.setIndexCreate(create);
            	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
            		if(!indexPath.endsWith(File.separator)){
            			indexPath += File.separator;
            		}
            		IndexString.setIndexDirectory(indexPath);
            	}
    			String nifStringOutput = IndexString.index(contentOrPath, docType, index, create, sFields, sAnalyzers, languageParam);
            	if(nifStringOutput!=null){
                    return ResponseGenerator.successResponse(nifStringOutput, "text/turtle");
            	}
            	else{
            		throw new ExternalServiceFailedException("ERROR at indexing document "+contentOrPath+" in index "+index);
            	}
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ExternalServiceFailedException(e.getMessage());
        }
    }

//	public boolean callLuceneRemover(String index, String field, String text) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
	public String getRepositoryInformation(String repositoryName) throws ExternalServiceFailedException {
		if(repositoryName==null || repositoryName.equals("")){
			throw new BadRequestException("No repository name given.");
		}
		try{
			IndexesRepository ir = new IndexesRepository(repositoryName);
			return ir.getListOfIndexes();
		}
		catch(Exception e){
			throw new ExternalServiceFailedException("ERROR retrieveing information form repository "+repositoryName);
		}
	}

	public String getIndexInformation(String repositoryName, String indexName) throws ExternalServiceFailedException,BadRequestException {
		if(repositoryName==null || repositoryName.equals("")){
			throw new BadRequestException("No repository name given.");
		}
		if(indexName==null || indexName.equals("")){
			throw new BadRequestException("No repository name given.");
		}
		try{
			IndexesRepository ir = new IndexesRepository(repositoryName);
			return ir.getIndexInformation(indexName);
		}
		catch(Exception e){
			throw new ExternalServiceFailedException("ERROR retrieveing information form repository "+repositoryName);
		}
	}
	
	public static void main(String[] args) {
	    ELuceneService service = new ELuceneService();
//	    service.callLuceneIndexing("string", " It has been correctly added to the tripletSTore: test1/", "NIF", "de","content", "standard","test1/",true);
		
	    String input = "<rdf:RDF"+
    " xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""+
    " xmlns:itsrdf=\"http://www.w3.org/2005/11/its/rdf#\""+
   " xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\""+
  "  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\""+
 "   xmlns:nif=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#\" > "+
"  <rdf:Description rdf:about=\"http://dkt.dfki.de/query#char=0,6\">"+
   " <rdf:type rdf:resource=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Context\"/>"+
  "  <rdf:type rdf:resource=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#String\"/>"+
 "   <rdf:type rdf:resource=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#RFC5147String\"/>"+
"    <nif:isString rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Madrid</nif:isString>"+
    "<nif:beginIndex rdf:datatype=\"http://www.w3.org/2001/XMLSchema#nonNegativeInteger\">0</nif:beginIndex>"+
   " <nif:endIndex rdf:datatype=\"http://www.w3.org/2001/XMLSchema#nonNegativeInteger\">6</nif:endIndex>"+
  " </rdf:Description>"+
 " <rdf:Description rdf:about=\"http://dkt.dfki.de/examples/#char=0,6\">"+
"    <nif:entity rdf:resource=\"http://dummy.LOC\"/>"+
   " <itsrdf:taIdentRef rdf:resource=\"http://dummy.com\"/>"+
  "  <nif:endIndex rdf:datatype=\"http://www.w3.org/2001/XMLSchema#nonNegativeInteger\">6</nif:endIndex>"+
 "   <nif:beginIndex rdf:datatype=\"http://www.w3.org/2001/XMLSchema#nonNegativeInteger\">0</nif:beginIndex>"+
"    <nif:anchorOf rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">Madrid</nif:anchorOf>"+
   " <rdf:type rdf:resource=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#RFC5147String\"/>"+
  "  <rdf:type rdf:resource=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#String\"/>"+
 " </rdf:Description>"+
" </rdf:RDF>";

	    ResponseEntity<String> resp = service.callLuceneExtraction("NIF", input, "de","test1/", "storage/", "content", "standard", 20);
	    System.out.println(resp.getBody());
	}

}
