# e-Lucene

The e-Lucene module performs full-text storage and retrieval of documents. It is an index storage system that allows users to store and retrieve documents based on full-text search.

**Please note** that in order to install and use this module, the code expects a folder for storage that has to be created manually. The location of this folder must be `/opt/storage/luceneStorage` and full reading and writing rights have to be assigned to it (e.g. `chmod 775 /opt/storage/luceneStorage`).

## Document Storage

The Storage of documents endpoint allows to store nif or plain text documents. 

### Endpoint

http://api.digitale-kuratierung.de/api/e-lucene/indexDocument

### Input
The API conforms to the general NIF API specifications. For more details, see: http://persistence.uni-leipzig.org/nlp2rdf/specification/api.html
In addition to the input, informat and outformat parameters, the following parameters have to be set to perform Semantic Information Storage:  

`indexName`: name of the sesame (triple storage) where the information must be stored.

`language`: 

`fields`: 

`analyzers`: 

### Output
A nif model containing information tot he index where the document has been stored.

### Example

Example cURL post for using the `document storage`:

> curl -X POST -d '@prefix dktnif: <http://dkt.dfki.de/ontologies/nif#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .
@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
<http://dkt.dfki.de/documents/#char=11,17>
        a                     nif:RFC5147String , nif:String ;
        nif:anchorOf          "Berlin"^^xsd:string ;
        nif:beginIndex        "11"^^xsd:nonNegativeInteger ;
        nif:endIndex          "17"^^xsd:nonNegativeInteger ;
        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,26> ;
        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .
<http://dkt.dfki.de/documents/#char=0,26>
        a               nif:RFC5147String , nif:String , nif:Context ;
        nif:beginIndex  "0"^^xsd:nonNegativeInteger ;
        nif:endIndex    "26"^^xsd:nonNegativeInteger ;
        nif:isString    "Welcome to Berlin in 2016."^^xsd:string .' "http://dev.digitale-kuratierung.de/api/e-lucene/indexDocument?indexName=lucene2&language=en&fields=all&analyzers=standard"

## Document Retrieval

The Retrieval of documents endpoint retrieves documents based on a plain text query. 

### Endpoint

http://api.digitale-kuratierung.de/api/e-lucene/retrieveDocuments

### Input
The API conforms to the general NIF API specifications. For more details, see: http://persistence.uni-leipzig.org/nlp2rdf/specification/api.html
In addition to the input, informat and outformat parameters, the following parameters have to be set to perform Semantic Information Retrieval:  

`indexName`: name of the lucene index where the information must be retrieved from.

`inputDataType`: parameter that specifies the format in which the query is provided to the service. It can have four different values: `NIF`, `entity`, `sparql` or `triple`.

`language`: language of the query. For now there are three available options: `en`, `es` or `de`.

`fields`: comma separated list of fields where the query should search on.

`analyzers`: comma separated list of analyzers that should be used for every fields. 

NOTE: the number of elements in the `fields` and `analyzers` parameters must be the same.

`hits`: integer defining the number of resutls that should be retrieved.

### Output
A NIF model with a self defined collection containing the retrieved documents.

### Example
Example cURL post for using the `semantic information storage`:  

> `curl -X POST "http://dev.digitale-kuratierung.de/api/e-lucene/retrieveDocuments?indexName=lucene2&inputType=text&language=en&fields=all&analyzers=standard&hits=50"`

