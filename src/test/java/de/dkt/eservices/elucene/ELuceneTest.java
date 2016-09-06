package de.dkt.eservices.elucene;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.hp.hpl.jena.rdf.model.Model;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import de.dkt.common.niftools.NIFManagement;
import de.dkt.common.niftools.NIFReader;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ELuceneTest {
	
	TestHelper testHelper;
	ValidationHelper validationHelper;

	@Before
	public void setup() {
		ApplicationContext context = IntegrationTestSetup
				.getContext(TestConstants.pathToPackage);
		testHelper = context.getBean(TestHelper.class);
		validationHelper = context.getBean(ValidationHelper.class);
	}
	
	private HttpRequestWithBody baseRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/testURL";
		return Unirest.post(url);
	}
	
	private GetRequest genericGetRequest(String path) {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/indexes"+path;
		return Unirest.get(url);
	}
	
	private HttpRequestWithBody genericPostRequest(String path) {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/indexes"+path;
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody genericDeleteRequest(String path) {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/indexes"+path;
		return Unirest.delete(url);
	}
	
	@Test
	public void test1_SanityCheck() throws UnirestException, IOException,
			Exception {
		HttpResponse<String> response = baseRequest()
				.queryString("informat", "text")
				.queryString("input", "hello world")
				.queryString("outformat", "turtle").asString();
		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
	}
	
	@Test
	public void test2_1_createIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index45")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("has been correctly generated"));
	}
	
	@Test
	@Transactional
	public void test2_2_deleteIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericDeleteRequest("/index45")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("correctly deleted"));
	}
	
	
	
	@Test
	public void test3_1_createIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("has been correctly generated"));
	}

	@Test
	public void test3_2_listIndexes() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericGetRequest("")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		System.out.println(response.getBody());
		Assert.assertTrue(response.getBody().contains("{\"indexes\":{\"index0\":\"index55\"}}"));
	}

	@Test
	public void test3_3_addDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55/documents")
				.queryString("language", "en")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.body(TestConstants.inputFile)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		try {
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
			Assert.assertNotNull(indexPath);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test3_4_addDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55/documents")
				.queryString("language", "en")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.body(TestConstants.inputFile)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		try {
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
			Assert.assertNotNull(indexPath);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test3_5_addDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55/documents")
				.queryString("language", "en")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.body(TestConstants.inputFile)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		try {
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
			Assert.assertNotNull(indexPath);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test3_6_retrieveDocuments() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericGetRequest("/index55/documents")
				.queryString("query", "Sanjurjo")
				.queryString("hits", "10")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		System.out.println("DEBUG retrieval output module: " + response.getBody());
		Assert.assertTrue(response.getBody().contains(""));
		
		//TODO 
		
//		Model collectionModel = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
//		List<Model> documents = NIFManagement.extractDocumentsModels(collectionModel);
//		System.out.println("DEBUG: documetns retrieved: "+documents.size());
//		assertTrue(documents.size() > 0);
	}

	@Test
	public void test3_7_deleteDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericDeleteRequest("/index55/documents")
				.queryString("documentId", "http://dkt.dfki.de/examples/#char=0,805")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		System.out.println(response.getBody());
		Assert.assertTrue(response.getBody().contains("has been deleted correctly"));
	}
	
	@Test
	public void test3_9_deleteIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericDeleteRequest("/index55")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("correctly deleted"));
	}
	
	@Test
	public void test4_1_createIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55GER")
				.queryString("language", "de")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("has been correctly generated"));
	}

	@Test
	public void test4_3_addDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55GER/documents")
				.queryString("language", "de")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.body(TestConstants.inputGermanFile)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		try {
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
			Assert.assertNotNull(indexPath);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test4_4_addDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericPostRequest("/index55GER/documents")
				.queryString("language", "de")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.body(TestConstants.inputGermanFile2)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		try {
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
			Assert.assertNotNull(indexPath);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test4_6_retrieveDocuments() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericGetRequest("/index55GER/documents")
				.queryString("query", "Berlin")
				.queryString("hits", "10")
				.queryString("informat", "text/turtle")
				.queryString("outformat", "text/turtle")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		System.out.println("DEBUG retrieval output module: " + response.getBody());
		Assert.assertTrue(response.getBody().contains(""));
		
		Model collectionModel = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
		List<Model> documents = NIFManagement.extractDocumentsModels(collectionModel);
		System.out.println("DEBUG: documetns retrieved: "+documents.size());
		assertTrue(documents.size() > 1);
	}

	@Test
	public void test4_9_deleteIndex() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = genericDeleteRequest("/index55GER")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertTrue(response.getBody().contains("correctly deleted"));
	}

//	@Test
//	public void test2_NIFDocumentStorage() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = storageRequest()
//				.queryString("informat", "turtle")
////				.queryString("input", TestConstants.inputFile)
//				.queryString("outformat", "turtle")
//				.queryString("indexName", "test2")
//				.queryString("fileType", "nif")
//				.queryString("language", "en")
//				.queryString("fields", "all")
//				.queryString("analyzers", "standard")
//				.body(TestConstants.inputFile)
//				.asString();
//		Assert.assertEquals(200, response.getStatus());
//		assertTrue(response.getBody().length() > 0);
//		
//		try {
////			Model mInput = NIFReader.extractModelFromFormatString(TestConstants.outputModel, RDFSerialization.TURTLE);
//			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
//			String indexPath = NIFReader.extractIndexNIFPath(mOutput);
//			Assert.assertNotNull(indexPath);
//		} catch (Exception e) {
//			Assert.assertTrue(false);
//		}
//	}
//	
//	@Test
//	public void test3_DocumentsRetrieval() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = retrievalRequest()
//				.queryString("informat", "text/plain")
//				.queryString("input", "Sanjurjo")
//				.queryString("outformat", "turtle")
//				.queryString("indexName", "test2")
//				.queryString("text", "Madrid")
//				.queryString("language", "en")
//				.queryString("fields", "all")
//				.queryString("analyzers", "standard")
//				.queryString("hits", 10)
//				.asString();
//		Assert.assertEquals(response.getStatus(), 200);
//		assertTrue(response.getBody().length() > 0);
//		System.out.println(response.getBody());
//		Model collectionModel = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
//		List<Model> documents = NIFManagement.extractDocumentsModels(collectionModel);
//		System.out.println("DEBUG: documetns retrieved: "+documents.size());
//		assertTrue(documents.size() > 0);
//		
//	}

}
