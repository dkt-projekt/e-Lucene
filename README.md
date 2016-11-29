# e-Lucene

The e-Lucene module performs full-text storage and retrieval of documents. It is an index storage system that allows users to store and retrieve documents based on full-text search.

**Please note** that in order to install and use this module, the code expects a folder for storage that has to be created manually. The location of this folder must be `/opt/storage/luceneStorage` and full reading and writing rights have to be assigned to it (e.g. `chmod 775 /opt/storage/luceneStorage `).

## List Indexes
Allows to check the available indexes.

### Endpoint
[GET] https://api.digitale-kuratierung.de/api/e-lucene/indexes

### Input
Nothing.

### Output
A json string containing information about the available indexes.

### Example
Example cURL get for getting the list of available indexes:

```
curl "https://dev.digitale-kuratierung.de/api/e-lucene/indexes"
```

## Create an Index
Allows to creation of an index in order to, later, add and index documents with it.

### Endpoint
[POST] https://api.digitale-kuratierung.de/api/e-lucene/indexes/{indexId}

### Input
`indexId`: Name for the lucene index that is implicitly given in the URL.
`language`: Language of the documents that will be included in the index.
`fields`: Comma  separated list of fields that will be created in the index. This parameter is directly related to the type of documents that are going to be indexed. For simplicity, the value `all` can be introduced and then the system will used all the possible fields for every type of document.
`analyzers`: Comma  separated list of analyzers which will be applied to every field. The number of element in the fields and analyzers list must be the same (exception thrown if it is different). The list of available analyzers are: `Standard` or `WhiteSpace`.
`overwrite`: Boolean value that defines if the index should be overwritten or deleted and new created in case of previous existence of an index with the same name.

### Output
A string specifying if the index has been correctly created or if there was an error occurring.

### Example

Example cURL post for using the `document storage`:

```
curl -X POST "https://dev.digitale-kuratierung.de/api/e-lucene/indexes/index101?language=en&fields=all&analyzers=standard"
```

## Delete an Index
Allows to delete an index.

### Endpoint
[DELETE] https://api.digitale-kuratierung.de/api/e-lucene/indexes/{indexId}

### Input
`indexId`: Name for the lucene index that is implicitly given in the URL.

### Output
A string specifying if the index has been correctly deleted or if there was an error ocurrring.

### Example
Example cURL DELETE request for deleting index `index101`:

```
curl -X DELETE "https://dev.digitale-kuratierung.de/api/e-lucene/indexes/index101"
```

## Add a Document to an Index
Allows the storage of documents into the specified index.

### Endpoint
[POST] https://api.digitale-kuratierung.de/api/e-lucene/indexes/{indexId}/documents

### Input
The API conforms to the general NIF API specifications. For more details, see: http://persistence.uni-leipzig.org/nlp2rdf/specification/api.html
In addition to the input, informat and outformat parameters, the following parameters have to be set to perform Semantic Information Storage:  

`indexId`: Name for the lucene index that is implicitly given in the URL.
`input` or `body` : The NIF content of the file to be indexed.

### Output
A nif model containing information of the index where the document has been stored.

### Example
Example cURL POST for using the `document storage`:

```
curl -X POST -d '@prefix dktnif: <http://dkt.dfki.de/ontologies/nif#> .
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
        nif:isString    "Welcome to Berlin in 2016."^^xsd:string .' "https://dev.digitale-kuratierung.de/api/e-lucene/indexes/index101/documents?language=en&fields=all&analyzers=standard"
```

## Delete a Document from an Index
Allows the deletion of documents from the specified index.

### Endpoint
[DELETE] https://api.digitale-kuratierung.de/api/e-lucene/indexes/{indexId}/documents

### Input

`indexId`: Name for the lucene index that is implicitly given in the URL.
`documentId`: The documentId refers to the documentURI that it had during indexing time.

### Output
A string specifying if the document has been correctly deleted or if there was an error ocurring.

### Example
Example cURL DELETE for deleting the document `http://dkt.dfki.de/documents/#char=0,26`:

```
curl -X DELETE "https://dev.digitale-kuratierung.de/api/e-lucene/indexes/index101/documents?documentId=http%3A%2F%2Fdkt.dfki.de%2Fdocuments%2F%23char%3D0%2C26"
```

## Retrieval of documents
The Retrieval of documents endpoint retrieves documents based on a plain text query. 

### Endpoint
[GET] https://api.digitale-kuratierung.de/api/e-lucene/indexes/{indexId}/documents

### Input

`indexId`: Name for the lucene index that is implicitly given in the URL.
`documentId`: The documentId referes to the documentURI that it had during indexing time.
`hits`: Integer defining the number of resutls that should be retrieved.
`outformat`: Output format of the NIF document.

### Output
A NIF model with a self defined collection containing the retrieved documents.

### Example
Example cURL GET for retrieving documents for query `Berlin` from index `index101`:  

```
curl -X GET "https://dev.digitale-kuratierung.de/api/e-lucene/indexes/index101/documents?query=Berlin&hits=50"
```
