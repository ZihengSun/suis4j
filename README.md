[![Build Status](https://travis-ci.org/juozasg/suis4j.svg?branch=refactoring)](https://travis-ci.org/juozasg/suis4j)

# suis4j

SUIS (Simple Universal Interface for Services) Java Library

# Introduction

The disparate interfaces of web-based spatial information and analysis services present high barriers of entry. Simple Universal Interface for Services (__SUIS4J__) simplifies the use of heterogeneous web services in Earth science applications.

__SUIS4J__ allow users to access geospatial web services without bogging down in technical details such as protocols, styles, bindings, schemas and addressing mechanisms. SUIS is a service-oriented framework that maps disparate interfaces onto a simple, user-friendly API.

__SUIS4J__ supports __SOAP__, __OGC__ and __REST__ spatial web services -- it does not replace existing interfaces but supplements them with a unified proxy API.

__SUIS4J__ seemlessly supports __Java__, __XML__ and __JSON__ representations of spatial web service data structures - permitting simple integration of existing application and technologies

The `SUIS4J` repository includes a comprehensive test suite and a prototype application that demonstrates SUIS4J capabilities for building geoprocessing workflows.



# Service Call Overview

1) Create `SUIS Client` and bind to service endpoint

2) Initialize a generic `Request Message` payload

3) Call remote service endpoint

4) Receive a generic `Response Message` payload

6) Use the `Response Message` in user application code


# Examples

## Basic Usage

### Create and bind a service

```
import org.suis4j.Client;

// Create a Client. See ServiceType.java for a list of supported services
Client restClient = new suis4j.Client(suis4j.ServiceType.REST_WADL);

// Initialize service schema for service from remote file
restClient.bindURL("https://service.iris.edu/irisws/timeseries/1/application.wadl");

```

### List service operations and operation parameters

```
// All operations
List<Operation> operations = client.getOperations();
List<String> operationNames = client.getOperationNames();

// Select operation
Operation operation = restClient.getOperation('subset_image');

// List operation request parameters
List <String> requestFields = operation.getRequestSchema().getFieldNames();

```


### Create a basic request message

```
Operation operation = restClient.getOperation('subset_image');
Message request = operation.getRequestMessage();
request.build()
	.set("imageurl", "http://example.org/large_image.jpeg")
	.set("response_format", "jpeg");

```

### Call service and read basic response
```
restClient.call(operation);
Message response = operation.getResponseMessage();

response.query().get("return_imageurl");
```

## Advanced Usage

### Connect to multiple services
```
Client restClient = new suis4j.Client(suis4j.ServiceType.REST_WADL);
Client soapClient = new suis4j.Client(suis4j.ServiceType.SOAP);
Client wcs2Client = new suis4j.Client(suis4j.ServiceType.OGC_WCS_2);

restClient.bindURL("https://service.iris.edu/irisws/timeseries/1/application.wadl");
soapClient.bindURL("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_Buffer_OGR?wsdl");
wcs2Client.bindURL("http://ows9.csiss.gmu.edu/cgi-bin/WCS20-r");
```

### Build requests with nested request message parameters (and XML attributes)
```
request.build()
	.set("imageurl", "http://example.org/large_image.jpeg")
	.element("boundingbox") // begin 'boundingbox'
		.attribute("coordinate_system", "WGS84")
		.set("top", "10")
		.set("left", "10")
		.set("bottom", "100")
		.element("right")
			.text("100")
		.end() // complete <right>
	.end() // complete <boundingbox>
	.set("response_format", "jpeg")
	.set("jpeg_quality", "80");
```

### Build request sfrom XML, JSON and Java HashMap 

```
request.build().fromJSON(text);
request.build().fromXML(text);
request.build().fromMap(javaHashMap);
```


### Override request parameters using dot notation
```
request.build().set("boundingbox.top", 	 "0");
```


### Build requests from combined Java and XML parameters
```
request.build()
	.set("imageurl", "http://example.org/large_image.jpeg")
	.element("boundingbox").fromXML(bbXMLText);
```

### Query response nested elements and XML attributes
```
Message response = operation.getResponseMessage();

response.query().get("returnurl");
response.query().element("boundingbox").get("top");
response.query().element("boundingbox").getAttribute("coordinate_system");
```


### Query recurrent response parameters  
```
List <String> supportedFormats = response.query().getAll('supported_format');
```

### Convert response into XML, JSON, Java HashMap
```
response.query().toJSON();
response.query().toXML();
response.query().toMap();
```


### Convert a subset of response message to JSON
```
response.query().element("boundingbox").toJSON();
// {"top": 10, "left": 10, "bottom": 100, "right": 100}
```


### Query response message using XPATH
```
String value = response.query().xpath("//boundingbox/top");
NodeList nodes = (NodeList) response.query().xpathNodeset("//boundingbox/top");
```

### Schema queries
```
// Get all top-level fields
List<String> fieldNames = request.getSchema().getFieldNames();

// Check if a field has sub-fields
request.getSchema().hasSubfields(fieldName);
request.getSchema().getFieldNames(fieldName);

// Convert request schema to XSD
request.getSchema().toXSD();
```


# Deployment

Import the suis4j library into your eclipse project classpath, or use Maven.

# Contributing


# Versioning


# Author

* **Ziheng Sun** - *Initial work* - [contact](https://zihengsun.com)
* **Juozas Gaigalas** - *API refactoring and test suite* - [contact](juozasgaigalas@gmail.com)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tips to everyone who's code was used
