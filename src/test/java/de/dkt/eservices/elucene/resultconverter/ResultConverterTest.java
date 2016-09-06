package de.dkt.eservices.elucene.resultconverter;

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
import de.dkt.eservices.elucene.TestConstants;
import de.dkt.eservices.elucene.indexmanagement.resultconverter.LuceneResultConverterToJENA;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResultConverterTest {
	
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
	public void test1_JSONLuceneResultConverter() throws UnirestException, IOException, Exception {
		
		//TODO
		
	}
	
	@Test
	public void test2_LuceneResultConverterToJENA() throws UnirestException, IOException, Exception {
		
		//TODO 
		
	}
	
}
