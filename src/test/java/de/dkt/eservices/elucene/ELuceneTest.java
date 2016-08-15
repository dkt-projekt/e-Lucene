package de.dkt.eservices.elucene;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;

import com.hp.hpl.jena.rdf.model.Model;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
	
	private HttpRequestWithBody storageRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/indexDocument";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody retrievalRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-lucene/retrieveDocuments";
		return Unirest.post(url);
	}
	
//	private HttpRequestWithBody indexInfoRequest() {
//		String url = testHelper.getAPIBaseUrl() + "/e-lucene/indexInfo";
//		return Unirest.post(url);
//	}
//	
//	private HttpRequestWithBody repositoryInfoRequest() {
//		String url = testHelper.getAPIBaseUrl() + "/e-lucene/repositoryInfo";
//		return Unirest.post(url);
//	}
	
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
	public void test2_NIFDocumentStorage() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = storageRequest()
				.queryString("informat", "turtle")
//				.queryString("input", TestConstants.inputFile)
				.queryString("outformat", "turtle")
				.queryString("indexName", "test1")
				.queryString("fileType", "nif")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.body(TestConstants.inputFile)
				.asString();
		Assert.assertEquals(200, response.getStatus());
		assertTrue(response.getBody().length() > 0);
		
		try {
			Model mInput = NIFReader.extractModelFromFormatString(TestConstants.outputModel, RDFSerialization.TURTLE);
			Model mOutput = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
			System.out.println("DEBUG: "+response.getBody());
//			Assert.assertTrue(true);
			Assert.assertTrue(mInput.isIsomorphicWith(mOutput));
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void test3_DocumentsRetrieval() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = retrievalRequest()
				.queryString("informat", "text/plain")
				.queryString("input", "Sanjurjo")
				.queryString("outformat", "turtle")
				.queryString("indexName", "test1")
				.queryString("text", "Madrid")
				.queryString("language", "en")
				.queryString("fields", "all")
				.queryString("analyzers", "standard")
				.queryString("hits", 10)
				.asString();
		Assert.assertEquals(response.getStatus(), 200);
		assertTrue(response.getBody().length() > 0);
		
		Model collectionModel = NIFReader.extractModelFromFormatString(response.getBody(), RDFSerialization.TURTLE);
		List<Model> documents = NIFManagement.extractDocumentsModels(collectionModel);
		System.out.println("DEBUG: documetns retrieved: "+documents.size());
		assertTrue(documents.size() > 0);
		
	}

}
