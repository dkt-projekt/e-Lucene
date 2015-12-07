/**
 * Copyright (C) 2015 3pc, Art+Com, Condat, Deutsches Forschungszentrum 
 * für Künstliche Intelligenz, Kreuzwerke (http://)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.broker.elucene.indexmanagement;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.freme.broker.elucene.exceptions.BadRequestException;
import eu.freme.broker.elucene.exceptions.ExternalServiceFailedException;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Handles index repositories and offers information about repositories and indexes
 * 
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class IndexesRepository {

	private Hashtable<String, Hashtable<String,String>> indexes;
	public IndexesRepository() {}

	/**
	 * Constructor that loads the repository information into the hashtable from the configFile.
	 * @param configFile XML file containing the information about the repository.
	 * @throws Exception
	 */
	public IndexesRepository(String configFile) throws Exception {
		if(configFile==null || configFile.equals("")){
			throw new BadRequestException("Bad argument: configFile must be provided.");
		}
		indexes = reloadRepositoryInformation(configFile,"xml");
	}

	/**
	 * @param configFile
	 * @param inputFormat
	 * @return
	 * @throws BadRequestException
	 */
	private Hashtable<String, Hashtable<String, String>> reloadRepositoryInformation(String configFile, String inputFormat) throws BadRequestException {
		if(inputFormat.equals("xml")){
			return reloadRepositoryInformationFromXML(configFile);
		}
		else{
			throw new BadRequestException("Bad argument: incorrect nput format");
		}
	}

	/**
	 * @param configFile
	 * @return
	 */
	private Hashtable<String, Hashtable<String, String>> reloadRepositoryInformationFromXML(String configFile) {
		Hashtable<String, Hashtable<String, String>> table = new Hashtable<String, Hashtable<String,String>>();
		try {
			ClassPathResource cpr = new ClassPathResource(configFile);
			File inputFile = cpr.getFile();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("index");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String iName = eElement.getAttribute("name");
					String iAnalyzer = eElement.getAttribute("analyzer");
					String iLanguage = eElement.getAttribute("language");
					NodeList fList = eElement.getElementsByTagName("field");
					Hashtable<String, String> fields = new Hashtable<String, String>();
					fields.put("property:language", iLanguage);
					fields.put("property:analyzer", iAnalyzer);
					for (int temp2 = 0; temp2 < fList.getLength(); temp2++) {
						Node fNode = fList.item(temp2);
						if (fNode.getNodeType() == Node.ELEMENT_NODE) {
							Element fElement = (Element) fNode;
							String fName = fElement.getAttribute("name");
							String fAnalyzer = fElement.getAttribute("analyzer");
							String fLanguage = fElement.getAttribute("language");
							fields.put("field:"+fName+"@"+fLanguage, fAnalyzer);
						}
					}
					table.put(iName, fields);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return table;
	}

	/**
	 * 
	 * @return String containing the list of available indexes in the repository.
	 */
	public String getListOfIndexes() {
		Enumeration<String> keys = indexes.keys();
		String s = "Indexes:=[";
		while(keys.hasMoreElements()){
			s = s + keys.nextElement() + ",";
		}
		s = s.substring(0, s.length()-1) + "]";
		return s;
	}

	/**
	 * Returns information about a specific index of the repository.
	 * @param name Name of the repository which information is going to be retrieved.
	 * @return Information about the specified repository
	 */
	public String getIndexInformation(String name) {
		if(name==null || name.equals("")){
			return "Bad input argument: null or empty index name";
		}
		if(indexes.containsKey(name)){
			String s = "";
			Hashtable<String,String> fields = indexes.get(name);
			Set<String> keys = fields.keySet();
			s = s + "IndexName: "+name+"\n";
			for (String k : keys) {
				s = s + "\t" + k + "\t\t" + fields.get(k) + "\n";					
			}
			return s;
		}
		else{
			return "No index available with this name.";
		}
	}

	/**
	 * @param iName
	 * @param iAnalyzer
	 * @param iLanguage
	 * @param fNames
	 * @param fAnalyzers
	 * @param fLanguages
	 * @return
	 */
	public boolean addIndex(String iName, String iAnalyzer, String iLanguage, String[] fNames, String[] fAnalyzers, String[] fLanguages){
		if(fNames.length!=fAnalyzers.length || fNames.length!=fLanguages.length){
			System.out.println("Incorrect number of field atributes");
			return false;
		}
		if(indexes==null){
			indexes = new Hashtable<String, Hashtable<String,String>>();
		}
		Hashtable<String, String> fields = new Hashtable<String, String>();
		fields.put("property:language", iLanguage);
		fields.put("property:analyzer", iAnalyzer);
		for (int i = 0; i < fNames.length; i++) {
			fields.put("field:"+fNames[i]+"@"+fLanguages[i], fAnalyzers[i]);
		}
		indexes.put(iName, fields);
		return true;
	}

	/**
	 * Removes a specified index from the repository information (It does not delete the index itself).
	 * @param name
	 * @return
	 */
	public boolean removeIndexFromRepository(String name){
		if(name==null || name.equals("")){
			System.out.println("Bad argument: empty name index or null");
			return false;
		}
		if(indexes.containsKey(name)){
			indexes.remove(name);
			return true;
		}
		else{
			System.out.println("Index "+name+" does not exist!!");
			return false;
		}
	}
	
	public boolean storeRepositoryInformation (String file){
		if(file==null || file.equals("")){
			return false;
		}
		ClassPathResource cpr = new ClassPathResource(file);
		
		//TODO
		
		
		return true;
	}
	
	/**
	 * @param args
	 * @throws ExternalServiceFailedException
	 */
	public static void main(String[] args) throws Exception{
		
		IndexesRepository ir = new IndexesRepository("indexes/repositoryInformation.xml");
		System.out.println(ir.getListOfIndexes());
		System.out.println(ir.getIndexInformation(""));
		System.out.println(ir.getIndexInformation(null));
		System.out.println(ir.getIndexInformation("prueba1"));
		System.out.println(ir.getIndexInformation("prueba2"));
		
	}


}
