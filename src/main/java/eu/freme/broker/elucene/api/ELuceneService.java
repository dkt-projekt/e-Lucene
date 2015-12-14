package eu.freme.broker.elucene.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.freme.broker.elucene.indexmanagement.IndexFiles;
import eu.freme.broker.elucene.indexmanagement.IndexString;
import eu.freme.broker.elucene.indexmanagement.IndexesRepository;
import eu.freme.broker.elucene.indexmanagement.SearchFiles;
import eu.freme.broker.exception.BadRequestException;
import eu.freme.broker.exception.ExternalServiceFailedException;
import eu.freme.broker.tools.ParameterChecker;
import eu.freme.broker.tools.ResponseGenerator;

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
	public ResponseEntity<String> callLuceneExtraction(String queryType, String text, String languageParam, String index, String sFields, String sAnalyzers,int hitsToReturn)//, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
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
        	String nif = SearchFiles.search(index, sFields, sAnalyzers, queryType, text, languageParam, hitsToReturn);
//            String nif = "We will return the document: " + text + " in the language: " + languageParam;
            return ResponseGenerator.successResponse(nif, "RDF/XML");
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ExternalServiceFailedException(e.getMessage());
        }
    }

    /**
     * @param path
     * @param languageParam
     * @param sFields
     * @param sAnalyzers
     * @param index
     * @param create
     * @return
     * @throws ExternalServiceFailedException
     * @throws BadRequestException
     */
    public ResponseEntity<String> callLuceneIndexing(String inputType, String path, String docType, String languageParam,String sFields,String sAnalyzers,String index,boolean create)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(inputType, "inputType [should be file/string] ");
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language");
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields");
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers");
    	ParameterChecker.checkNotNullOrEmpty(index, "index");
    	ParameterChecker.checkNotNullOrEmpty(path, "document path");
    	System.out.println(inputType);
    	System.out.println(languageParam);
    	System.out.println(sFields);
    	System.out.println(sAnalyzers);
    	System.out.println(index);
    	System.out.println(path);
    	System.out.println(create);
    	System.out.println();
    	try {
    		if(inputType.equalsIgnoreCase("file")){
            	if(IndexFiles.index(path, docType, index, create, sFields, sAnalyzers, languageParam)){
            		String nif = "Document: " + path + " in language: " + languageParam + "has been correctly indexed in index: "+index;
                    return ResponseGenerator.successResponse(nif, "RDF/XML");
            	}
            	else{
            		throw new ExternalServiceFailedException("ERROR at indexing document "+path+" in index "+index);
            	}
    		}
    		else{
            	if(IndexString.index(path, docType, index, create, sFields, sAnalyzers, languageParam)){
            		String nif = "Document: " + path + " in language: " + languageParam + "has been correctly indexed in index: "+index;
                    return ResponseGenerator.successResponse(nif, "RDF/XML");
            	}
            	else{
            		throw new ExternalServiceFailedException("ERROR at indexing document "+path+" in index "+index);
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

	    service.callLuceneExtraction("NIF", input, "de","test1/", "content", "standard", 20);

	}

}
