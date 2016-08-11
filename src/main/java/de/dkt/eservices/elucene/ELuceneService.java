package de.dkt.eservices.elucene;

import java.io.File;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.tools.ParameterChecker;
import de.dkt.eservices.elucene.indexmanagement.IndexesRepository;
import de.dkt.eservices.elucene.indexmanagement.IndexingModule;
import de.dkt.eservices.elucene.indexmanagement.SearchFiles;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
@Component
public class ELuceneService {
    
	Logger logger = Logger.getLogger(ELuceneService.class);

	/**
	 * @param text
	 * @param languageParam
	 * @param index
	 * @return
	 * @throws ExternalServiceFailedException
	 * @throws BadRequestException
	 */
	public JSONObject callLuceneExtraction(String queryType, String text, String languageParam, String index, String indexPath, String sFields, String sAnalyzers,int hitsToReturn)//, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
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
        		SearchFiles.setIndexDirectory(indexPath);
        	}
        	JSONObject nifOutputModel = SearchFiles.search(index, sFields, sAnalyzers, queryType, text, languageParam, hitsToReturn);
            return nifOutputModel;
        } catch (Exception e) {
//        	e.printStackTrace();
        	logger.error(e.getMessage());
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
    public Model indexDocument(Model inModel, String languageParam,String sFields,String sAnalyzers,
    		String index,String indexPath,boolean create)
            throws ExternalServiceFailedException, BadRequestException {
    	ParameterChecker.checkNotNullOrEmpty(languageParam, "language", logger);
    	ParameterChecker.checkNotNullOrEmpty(sFields, "fields", logger);
    	ParameterChecker.checkNotNullOrEmpty(sAnalyzers, "analyzers", logger);
    	ParameterChecker.checkNotNullOrEmpty(index, "index", logger);
    	if(inModel==null){
    		String msg = "Input model is NULL";
    		logger.error(msg);
    		throw new BadRequestException(msg);
    	}
    	try {
        	IndexingModule.setIndexCreate(create);
        	if(indexPath!=null && !indexPath.equalsIgnoreCase("")){
        		if(!indexPath.endsWith(File.separator)){
        			indexPath += File.separator;
        		}
        		IndexingModule.setIndexDirectory(indexPath);
        	}
			Model nifModelOutput = IndexingModule.indexModel(inModel, indexPath, sFields, sAnalyzers, languageParam);
        	if(nifModelOutput!=null){
                return nifModelOutput;
        	}
        	else{
        		String msg = "ERROR at indexing document in index "+index;
        		logger.error(msg);
        		throw new ExternalServiceFailedException(msg);
        	}
        } catch (Exception e) {
            logger.error(e.getMessage());
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
			logger.error("No repository name given.");
			throw new BadRequestException("No repository name given.");
		}
		try{
			IndexesRepository ir = new IndexesRepository(repositoryName);
			return ir.getListOfIndexes();
		}
		catch(Exception e){
			logger.error("ERROR retrieveing information form repository "+repositoryName);
			throw new ExternalServiceFailedException("ERROR retrieveing information form repository "+repositoryName);
		}
	}

	public String getIndexInformation(String repositoryName, String indexName) throws ExternalServiceFailedException,BadRequestException {
		if(repositoryName==null || repositoryName.equals("")){
			logger.error("No repository name given.");
			throw new BadRequestException("No repository name given.");
		}
		if(indexName==null || indexName.equals("")){
			logger.error("No index name given.");
			throw new BadRequestException("No index name given.");
		}
		try{
			IndexesRepository ir = new IndexesRepository(repositoryName);
			return ir.getIndexInformation(indexName);
		}
		catch(Exception e){
			logger.error("ERROR retrieveing information form repository "+repositoryName);
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

	    JSONObject resp = service.callLuceneExtraction("NIF", input, "de","test1/", "storage/", "content", "standard", 20);
	    System.out.println(resp);
	}

}
