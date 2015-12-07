package eu.freme.broker.elucene.api;

import org.springframework.stereotype.Component;

import eu.freme.broker.elucene.exceptions.BadRequestException;
import eu.freme.broker.elucene.exceptions.ExternalServiceFailedException;
import eu.freme.broker.elucene.indexmanagement.IndexFiles;
import eu.freme.broker.elucene.indexmanagement.IndexesRepository;
import eu.freme.broker.elucene.indexmanagement.SearchFiles;

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
	public String callLuceneExtraction(String text, String languageParam, String index, String sFields, String sAnalyzers,int hitsToReturn)//, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
            throws ExternalServiceFailedException, BadRequestException {
    	if(languageParam==null || languageParam.equals("")){
            throw new BadRequestException("Bad request: no language specified");
    	}
//    	if(fields==null){
//            throw new BadRequestException("Bad request: no fields specified");
//    	}
    	if(index==null || index.equals("")){
            throw new BadRequestException("Bad request: no index specified");
    	}
    	if(text==null || text.equals("")){
            throw new BadRequestException("Bad request: no query text specified");
    	}
        try {
//            System.out.println(text);
//            System.out.println(URLDecoder.decode(text, "UTF-8"));
//            String nif = "Test for the service to be working";
        	String nif = SearchFiles.search(index, sFields, sAnalyzers, text, hitsToReturn);
//            String nif = "We will return the document: " + text + " in the language: " + languageParam;
            return nif;
        } catch (Exception e) {
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
    public String callLuceneIndexing(String path, String docType, String languageParam,String sFields,String sAnalyzers,String index,boolean create)
            throws ExternalServiceFailedException, BadRequestException {
    	if(languageParam==null || languageParam.equals("")){
            throw new BadRequestException("Bad request: no language specified");
    	}
    	if(sFields==null){
            throw new BadRequestException("Bad request: no fields specified");
    	}
    	if(sAnalyzers==null){
            throw new BadRequestException("Bad request: no analyzers specified");
    	}
    	if(index==null || index.equals("")){
            throw new BadRequestException("Bad request: no index specified");
    	}
    	if(path==null || path.equals("")){
            throw new BadRequestException("Bad request: no document path specified");
    	}
        try {
        	if(IndexFiles.index(path, docType, index, create, sFields, sAnalyzers, languageParam)){

        		//TODO We must generate a NIF output containing  unique identifier for the document.
        		
        		String nif = "Document: " + path + " in language: " + languageParam + "has been correctly indexed in index: "+index;
                return nif;
        	}
        	else{
        		throw new ExternalServiceFailedException("ERROR at indexing document "+path+" in index "+index);
        	}
        } catch (Exception e) {
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

}
