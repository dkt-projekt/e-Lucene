package de.dkt.eservices.elucene;

public class TestConstants {
	
	public static final String pathToPackage = "rdftest/elucene-test-package.xml";
	
	public static String inputFile = "@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
			"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" +
			"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
			"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
			"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=494,505>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"5 September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"494\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"505\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=0,805>\n" +
			"        a                    nif:RFC5147String , nif:String , nif:Context ;\n" +
			"        nif:beginIndex       \"0\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:centralGeoPoint  \"42.039727380952385_-4.008917460317461\"^^xsd:string ;\n" +
			"        nif:endIndex         \"805\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:geoStandardDevs  \"1.421155171522152_1.8662593699060581\"^^xsd:string ;\n" +
			"        nif:isString         \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"^^xsd:string ;\n" +
			"        nif:meanDateRange    \"19360531090000_19361022090000\"^^xsd:string .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=399,403>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"399\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"403\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=407,416>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"407\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"416\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=146,151>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"South\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"146\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"151\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Southern_United_States> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=636,642>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Madrid\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"636\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"642\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"40.4_-3.6833333333333336\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Madrid> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=0,4>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"1936\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"0\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"4\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=156,163>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"21 July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"156\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"163\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=277,282>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Spain\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"277\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"282\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.0_-4.0\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Spain> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=788,804>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"end of September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"788\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"804\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=598,610>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"13 September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"598\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"610\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=58,65>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"20 July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"58\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"65\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=254,260>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Ferrol\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"254\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"260\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.46666666666667_-8.25\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Ferrol,_Galicia> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=704,710>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Bilbao\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"704\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"710\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.25694444444444_-2.923611111111111\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Bilbao> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=543,547>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Irun\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"543\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"547\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.33781388888889_-1.788811111111111\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Irun> .\n" +
			"";

	public static String outputModel = "@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
			"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" +
			"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
			"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
			"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=494,505>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"5 September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"494\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"505\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=0,805>\n" +
			"        a                    nif:RFC5147String , nif:String , nif:Context ;\n" +
			"        nif:beginIndex       \"0\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:centralGeoPoint  \"42.039727380952385_-4.008917460317461\"^^xsd:string ;\n" +
			"        nif:endIndex         \"805\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:geoStandardDevs  \"1.421155171522152_1.8662593699060581\"^^xsd:string ;\n" +
	        "        nif:indexName        \"test1\"^^xsd:string ;\n" + 
//	        "        nif:indexPath        \"/Users/jumo04/Documents/DFKI/DKT/dkt-test/testTimelining/luceneStorage/\"^^xsd:string ;\n" + 
			"        nif:indexPath        \"LUCENESTORAGE\"^^xsd:string ;\n" + 
			"        nif:isString         \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"^^xsd:string ;\n" +
			"        nif:meanDateRange    \"19360531090000_19361022090000\"^^xsd:string .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=399,403>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"399\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"403\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=407,416>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"407\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"416\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=146,151>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"South\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"146\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"151\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Southern_United_States> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=636,642>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Madrid\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"636\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"642\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"40.4_-3.6833333333333336\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Madrid> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=0,4>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"1936\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"0\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"4\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=156,163>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"21 July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"156\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"163\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=277,282>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Spain\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"277\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"282\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.0_-4.0\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Spain> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=788,804>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"end of September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"788\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"804\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=598,610>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"13 September\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"598\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"610\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=58,65>\n" +
			"        a                  nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf       \"20 July\"^^xsd:string ;\n" +
			"        nif:beginIndex     \"58\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex       \"65\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
			"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=254,260>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Ferrol\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"254\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"260\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.46666666666667_-8.25\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Ferrol,_Galicia> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=704,710>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Bilbao\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"704\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"710\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.25694444444444_-2.923611111111111\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Bilbao> .\n" +
			"\n" +
			"<http://dkt.dfki.de/examples/#char=543,547>\n" +
			"        a                     nif:RFC5147String , nif:String ;\n" +
			"        nif:anchorOf          \"Irun\"^^xsd:string ;\n" +
			"        nif:beginIndex        \"543\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:endIndex          \"547\"^^xsd:nonNegativeInteger ;\n" +
			"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
			"        nif:geoPoint          \"43.33781388888889_-1.788811111111111\"^^xsd:string ;\n" +
			"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
			"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Irun> .\n" +
			"";

	public static String expectedOutput = "@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" +
"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=494,505>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"5 September\"^^xsd:string ;\n" +
"        nif:beginIndex     \"494\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"505\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360905000000_19360906000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=0,805>\n" +
"        a                    nif:RFC5147String , nif:String , nif:Context ;\n" +
"        nif:beginIndex       \"0\"^^xsd:nonNegativeInteger ;\n" +
"        nif:centralGeoPoint  \"42.039727380952385_-4.008917460317461\"^^xsd:string ;\n" +
"        nif:endIndex         \"805\"^^xsd:nonNegativeInteger ;\n" +
"        nif:geoStandardDevs  \"1.421155171522152_1.8662593699060581\"^^xsd:string ;\n" +
"        nif:indexName        \"test1\"^^xsd:string ;\n" +
"        nif:indexPath        \"storage/\"^^xsd:string ;\n" +
"        nif:isString         \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"^^xsd:string ;\n" +
"        nif:meanDateRange    \"19360531090000_19361022090000\"^^xsd:string .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=399,403>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"July\"^^xsd:string ;\n" +
"        nif:beginIndex     \"399\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"403\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360701000000_19360702000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=407,416>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"September\"^^xsd:string ;\n" +
"        nif:beginIndex     \"407\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"416\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360901000000_19360902000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=146,151>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"South\"^^xsd:string ;\n" +
"        nif:beginIndex        \"146\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"151\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Southern_United_States> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=636,642>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"Madrid\"^^xsd:string ;\n" +
"        nif:beginIndex        \"636\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"642\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:geoPoint          \"40.4_-3.6833333333333336\"^^xsd:string ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Madrid> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=0,4>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"1936\"^^xsd:string ;\n" +
"        nif:beginIndex     \"0\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"4\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360101000000_19370101000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=156,163>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"21 July\"^^xsd:string ;\n" +
"        nif:beginIndex     \"156\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"163\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360721000000_19360722000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=277,282>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"Spain\"^^xsd:string ;\n" +
"        nif:beginIndex        \"277\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"282\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:geoPoint          \"43.0_-4.0\"^^xsd:string ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Spain> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=58,65>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"20 July\"^^xsd:string ;\n" +
"        nif:beginIndex     \"58\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"65\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360720000000_19360721000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=788,804>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"end of September\"^^xsd:string ;\n" +
"        nif:beginIndex     \"788\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"804\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360920000000_19360930000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=598,610>\n" +
"        a                  nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf       \"13 September\"^^xsd:string ;\n" +
"        nif:beginIndex     \"598\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex       \"610\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=19360913000000_19360914000000> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=254,260>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"Ferrol\"^^xsd:string ;\n" +
"        nif:beginIndex        \"254\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"260\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:geoPoint          \"43.46666666666667_-8.25\"^^xsd:string ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Ferrol,_Galicia> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=704,710>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"Bilbao\"^^xsd:string ;\n" +
"        nif:beginIndex        \"704\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"710\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:geoPoint          \"43.25694444444444_-2.923611111111111\"^^xsd:string ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Bilbao> .\n" +
"\n" +
"<http://dkt.dfki.de/examples/#char=543,547>\n" +
"        a                     nif:String , nif:RFC5147String ;\n" +
"        nif:anchorOf          \"Irun\"^^xsd:string ;\n" +
"        nif:beginIndex        \"543\"^^xsd:nonNegativeInteger ;\n" +
"        nif:endIndex          \"547\"^^xsd:nonNegativeInteger ;\n" +
"        nif:entity            <http://dkt.dfki.de/ontologies/nif#location> ;\n" +
"        nif:geoPoint          \"43.33781388888889_-1.788811111111111\"^^xsd:string ;\n" +
"        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,805> ;\n" +
"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Irun> .\n" +
			"";
	
	public static String expectedRetrievalOutput = "{\"results\":{\"documents\":{\"document1000\":{\"score\":0.028767451643943787,\"docId\":1,"
			+ "\"content\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an "
			+ "effective command split between Mola in the North and Franco in the South. On 21 July, the "
			+ "fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol "
			+ "in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, "
			+ "undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the "
			+ "Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, "
			+ "closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid "
			+ "to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on "
			+ "the border of Viscaya halted these forces at the end of September.\\n\"},"
			+ "\"document1\":{\"score\":0.028767451643943787,\"docId\":1,\"content\":\"1936\\n\\nCoup leader "
			+ "Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between "
			+ "Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the "
			+ "Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel "
			+ "force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of "
			+ "Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces "
			+ "in the north. On 5 September, after heavy fighting the force took Irun, closing the French "
			+ "border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, "
			+ "who then advanced toward their capital, Bilbao. The Republican militias on the border of "
			+ "Viscaya halted these forces at the end of September.\\n\"}},\"numberResults\":1,"
			+ "\"querytext\":\"content:madrid\"}}";
}
