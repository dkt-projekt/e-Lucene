package de.dkt.eservices.elucene.documentparser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
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
import de.dkt.eservices.elucene.exceptions.QueryTypeNotSupportedException;
import de.dkt.eservices.elucene.exceptions.UnSupportedDocumentParserFormatException;
import de.dkt.eservices.elucene.indexmanagement.documentparser.DocumentParserFactory;
import de.dkt.eservices.elucene.indexmanagement.documentparser.IDocumentParser;
import de.dkt.eservices.elucene.indexmanagement.documentparser.NIFDocumentParser;
import de.dkt.eservices.elucene.indexmanagement.documentparser.TXTDocumentParser;
import de.dkt.eservices.elucene.indexmanagement.queryparser.IQueryParser;
import de.dkt.eservices.elucene.indexmanagement.queryparser.OwnQueryParser;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentParserTest {
	
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
	public void test1_DocumentParserFactory() throws UnirestException, IOException, Exception {
		IDocumentParser dP = DocumentParserFactory.getDocumentParser("nif");
		Assert.assertNotNull(dP);
		Assert.assertTrue(dP instanceof NIFDocumentParser);
		
		dP = DocumentParserFactory.getDocumentParser("txt");
		Assert.assertNotNull(dP);
		Assert.assertTrue(dP instanceof TXTDocumentParser);

		try {
			dP = DocumentParserFactory.getDocumentParser("");
			Assert.assertTrue(false);
		} catch (UnSupportedDocumentParserFormatException e) {
			Assert.assertTrue(true);
		}

		try {
			dP = DocumentParserFactory.getDocumentParser(null);
			Assert.assertTrue(false);
		} catch (UnSupportedDocumentParserFormatException e) {
			Assert.assertTrue(true);
		}

		try {
			dP = DocumentParserFactory.getDocumentParser("NON VALID VALUE");
			Assert.assertTrue(false);
		} catch (UnSupportedDocumentParserFormatException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void test2_NIFParser_FromString() throws UnirestException, IOException, Exception {
		IDocumentParser dP = DocumentParserFactory.getDocumentParser("nif");
		String[] fields = {"all"};
		Model inputModel = NIFReader.extractModelFromFormatString(TestConstants.inputFile, RDFSerialization.TURTLE);
//		String path = "";
//		
//		Document d1 = dP.parseDocumentFromFile(path, fields);

	
		Document d2 = dP.parseDocumentFromString(TestConstants.inputFile, fields);
		
//		System.out.println("--->" + d2.getField("content"));
//		System.out.println("--->" + d2.getField("entities"));
//		System.out.println("--->" + d2.getField("links"));
//		System.out.println("--->" + d2.getField("temporal"));
//		System.out.println("--->" + d2.getField("nifcontent"));
//		System.out.println("--->" + d2.getField("docURI").stringValue());
//		System.out.println("--->" + d2.getField("docURI").fieldType());
	
		Assert.assertEquals("1936\n"+
"\n"+
"Coup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n"+
"", d2.getField("content").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("content").fieldType().toString());
		Assert.assertEquals("5 September;July;September;13 September;Bilbao;1936;South;Irun;end of September;20 July;Ferrol;Spain;Madrid;21 July", 
				d2.getField("entities").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("entities").fieldType().toString());
		Assert.assertEquals("http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000;http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000;http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000;http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000;http://dbpedia.org/resource/Bilbao;http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000;http://dbpedia.org/resource/Southern_United_States;http://dbpedia.org/resource/Irun;http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000;http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000;http://dbpedia.org/resource/Ferrol,_Galicia;http://dbpedia.org/resource/Spain;http://dbpedia.org/resource/Madrid;http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000", 
				d2.getField("links").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("links").fieldType().toString());
		Assert.assertEquals("", d2.getField("temporal").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("temporal").fieldType().toString());
		Model m2 = NIFReader.extractModelFromFormatString(d2.getField("nifcontent").stringValue(), RDFSerialization.TURTLE);
		Assert.assertTrue(m2.isIsomorphicWith(inputModel));
//		Assert.assertEquals(TestConstants.inputFile, d2.getField("nifcontent").stringValue());
		Assert.assertEquals("stored", d2.getField("nifcontent").fieldType().toString());
		Assert.assertEquals(NIFReader.extractDocumentWholeURI(inputModel), d2.getField("docURI").stringValue());
		Assert.assertEquals("stored", d2.getField("docURI").fieldType().toString());

	}
	
	@Test
	public void test2_NIFParser_FromModel() throws UnirestException, IOException, Exception {
		IDocumentParser dP = DocumentParserFactory.getDocumentParser("nif");
		String[] fields = {"all"};
		Model inputModel = NIFReader.extractModelFromFormatString(TestConstants.inputFile, RDFSerialization.TURTLE);
		Document d2 = dP.parseDocumentFromModel(inputModel, fields);
		Assert.assertEquals("1936\n"+
"\n"+
"Coup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n"+
"", d2.getField("content").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("content").fieldType().toString());
		Assert.assertEquals("5 September;July;September;13 September;Bilbao;1936;South;Irun;end of September;20 July;Ferrol;Spain;Madrid;21 July", 
				d2.getField("entities").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("entities").fieldType().toString());
		Assert.assertEquals("http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000;http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000;http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000;http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000;http://dbpedia.org/resource/Bilbao;http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000;http://dbpedia.org/resource/Southern_United_States;http://dbpedia.org/resource/Irun;http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000;http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000;http://dbpedia.org/resource/Ferrol,_Galicia;http://dbpedia.org/resource/Spain;http://dbpedia.org/resource/Madrid;http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000", 
				d2.getField("links").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("links").fieldType().toString());
		Assert.assertEquals("", d2.getField("temporal").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("temporal").fieldType().toString());
		Model m2 = NIFReader.extractModelFromFormatString(d2.getField("nifcontent").stringValue(), RDFSerialization.TURTLE);
		Assert.assertTrue(m2.isIsomorphicWith(inputModel));
//		Assert.assertEquals(TestConstants.inputFile, d2.getField("nifcontent").stringValue());
		Assert.assertEquals("stored", d2.getField("nifcontent").fieldType().toString());
		Assert.assertEquals(NIFReader.extractDocumentWholeURI(inputModel), d2.getField("docURI").stringValue());
		Assert.assertEquals("stored", d2.getField("docURI").fieldType().toString());

	}
	
	@Test
	public void test4_TXTParser() throws UnirestException, IOException, Exception {
		IDocumentParser dP = DocumentParserFactory.getDocumentParser("nif");
		String[] fields = {"all"};
		Model inputModel = NIFReader.extractModelFromFormatString(TestConstants.inputFile, RDFSerialization.TURTLE);
		Document d2 = dP.parseDocumentFromModel(inputModel, fields);
		Assert.assertEquals("1936\n"+
"\n"+
"Coup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n"+
"", d2.getField("content").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("content").fieldType().toString());
		Assert.assertEquals("5 September;July;September;13 September;Bilbao;1936;South;Irun;end of September;20 July;Ferrol;Spain;Madrid;21 July", 
				d2.getField("entities").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("entities").fieldType().toString());
		Assert.assertEquals("http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000;http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000;http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000;http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000;http://dbpedia.org/resource/Bilbao;http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000;http://dbpedia.org/resource/Southern_United_States;http://dbpedia.org/resource/Irun;http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000;http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000;http://dbpedia.org/resource/Ferrol,_Galicia;http://dbpedia.org/resource/Spain;http://dbpedia.org/resource/Madrid;http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000", 
				d2.getField("links").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("links").fieldType().toString());
		Assert.assertEquals("", d2.getField("temporal").stringValue());
		Assert.assertEquals("stored,indexed,tokenized", d2.getField("temporal").fieldType().toString());
		Model m2 = NIFReader.extractModelFromFormatString(d2.getField("nifcontent").stringValue(), RDFSerialization.TURTLE);
		Assert.assertTrue(m2.isIsomorphicWith(inputModel));
//		Assert.assertEquals(TestConstants.inputFile, d2.getField("nifcontent").stringValue());
		Assert.assertEquals("stored", d2.getField("nifcontent").fieldType().toString());
		Assert.assertEquals(NIFReader.extractDocumentWholeURI(inputModel), d2.getField("docURI").stringValue());
		Assert.assertEquals("stored", d2.getField("docURI").fieldType().toString());


	}
	
}
