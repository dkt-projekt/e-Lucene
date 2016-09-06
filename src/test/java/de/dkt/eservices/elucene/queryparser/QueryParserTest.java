package de.dkt.eservices.elucene.queryparser;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;

import com.mashape.unirest.http.exceptions.UnirestException;

import de.dkt.eservices.elucene.TestConstants;
import de.dkt.eservices.elucene.exceptions.QueryTypeNotSupportedException;
import de.dkt.eservices.elucene.indexmanagement.queryparser.IQueryParser;
import de.dkt.eservices.elucene.indexmanagement.queryparser.OwnQueryParser;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryParserTest {
	
	TestHelper testHelper;
	ValidationHelper validationHelper;

	@Before
	public void setup() {
		ApplicationContext context = IntegrationTestSetup
				.getContext(TestConstants.pathToPackage);
		testHelper = context.getBean(TestHelper.class);
		validationHelper = context.getBean(ValidationHelper.class);
	}
	
	@Test
	public void test1_OwnQueryParser_Constructor() throws UnirestException, IOException, Exception {
		IQueryParser parser = new OwnQueryParser();
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void test2_OwnQueryParser_DocumentIdQuery() throws UnirestException, IOException, Exception {
		Query q = OwnQueryParser.parseDocumentIdQuery("http://dkt.dfki.de/collection1/document2");
		Assert.assertEquals("documentId:http://dkt.dfki.de/collection1/document2",q.toString());
	}
	
	@Test
	public void test3_OwnQueryParser_Query() throws UnirestException, IOException, Exception {
		String [] fields = {"field1","field2"};
		String [] analyzers = {"standard","standard"};
		Query q = OwnQueryParser.parseQuery("text", fields, analyzers, "en");
		Assert.assertEquals("field1:text field2:text",q.toString());
	}
	
	@Test
	public void test4_OwnQueryParser_complexQuery() throws UnirestException, IOException, Exception {
		String [] fields = {"field1","field2"};
		String [] analyzers = {"standard","standard"};
		
//		//nif
//		Query q1 = OwnQueryParser.parseComplexQuery("nif", "text", fields, analyzers, "en");
//		Assert.assertEquals("field1:text field2:text",q1.toString());
//	
		//docid
		Query q2 = OwnQueryParser.parseComplexQuery("docid", "text", fields, analyzers, "en");
		Assert.assertEquals("field1:text field2:text",q2.toString());
	
		//tfidf
		Query q3 = OwnQueryParser.parseComplexQuery("tfidf", "text", fields, analyzers, "en");
		Assert.assertEquals("field1:text field2:text",q3.toString());
	
		//plaintext
		Query q4 = OwnQueryParser.parseComplexQuery("plaintext", "text", fields, analyzers, "en");
		Assert.assertEquals("field1:text field2:text",q4.toString());
	
		try {
			OwnQueryParser.parseComplexQuery("othervalue", "text", fields, analyzers, "en");
			Assert.assertTrue(false);
		} catch (QueryTypeNotSupportedException e) {
			Assert.assertTrue(true);
		}
	}
			
}
