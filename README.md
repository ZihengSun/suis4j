# suis4j

SUIS (Simple Universal Interface for Services) Java Library

# Introduction

The disparate interfaces of web-based spatial information and analysis services cause very high barriers of entry. Simple Universal Interface for Services (__SUIS4J__) simplifies the use of heterogeneous web services in Earth science applications.

__SUIS4J__ allow users to access geospatial web services without bogging down in technical details such as protocols, styles, bindings, schemas and addressing mechanisms. SUIS is a service-oriented framework that maps disparate interfaces onto a simple, user-friendly API.

__SUIS4J__ supports __SOAP__, __OGC__ and __REST__ spatial web services -- it does not replace existing interfaces but supplements them with a unified proxy. 

The `SUIS4J` repository includes a comprehensive test suite and a prototype application that demonstrates SUIS4J capabilities for building geoprocessing workflows.



# Basic Steps for Service Calls:

1) Create the _SUIS request_ message

2) Encode the _SUIS request_ to _service request_ payload

3) Send the _service request_ payload to service endpoint

4) Receive the _service response_ payload from service endpoint

5) Decode _service response_ payload to _SUIS response_ message

6) Use the _SUIS reponse_ in application workflow logic


# Code Examples

Example code of calling RESTful service:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("https://service.iris.edu/irisws/timeseries/1/application.wadl", ServiceType.REST)
	.build(); 
		
sc.listOperations();
		
Operation o = sc.operation("http://service.iris.edu/timeseries/1/version");

sc.listInputParams(o);

sc.listOutputParams(o);

o.getInput()
	.value("network", "IU")
	.value("station", "ANMO")
	.value("location", "00")
	.value("channel", "BHZ")
	.value("starttime", "2001-12-09T12:00:00")
	.value("endtime", "2001-12-09T12:20:00")
	.value("output", "plot");
	
Message outm = sc.call(o);
		
sc.listOutputValues(outm);
```

An example code of calling SOAP service:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_Buffer_OGR?wsdl", ServiceType.SOAP)
	.build(); 
		
sc.listOperations();
		
Operation o = sc.operation("buffer");
		
sc.listInputParams(o);
		
sc.listOutputParams(o);

o.getInput()
	.value("sourceURL", "http://www3.csiss.gmu.edu/data/building.zip")
	.value("buffer", 100);
	
Message outm = sc.call(o);
		
sc.listOutputValues(outm);
```

An example code of calling WPS 1.0.0:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("http://geoprocessing.demo.52north.org/latest-wps/WebProcessingService?Request=GetCapabilities&Service=WPS&version=1.0.0", ServiceType.OGC)
	.build(); 
		
sc.listOperations();
		
Operation o = sc.operation("org.n52.wps.server.algorithm.SimpleBufferAlgorithm");
		
sc.listInputParams(o);

sc.listOutputParams(o);

o.getInput().value("data", "http://geoprocessing.demo.52north.org:8080/geoserver/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=topp:tasmania_roads&SRS=EPSG:4326&OUTPUTFORMAT=GML3")
	.value("width", 0.05);
	
Message outm = sc.call(o);
		
sc.listOutputValues(outm);

```

An example code of calling WCS 2.0.0:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("http://ows9.csiss.gmu.edu/cgi-bin/WCS20-r?service=WCS&version=2.0.0&request=GetCapabilities", ServiceType.OGC)
	.build(); 

sc.listOperations();
		
Operation o = sc.operation("GetCoverage");
		
sc.listInputParams(o);

sc.listOutputParams(o);

o.getInput().value("coverageId", "GEOTIFF:\"/home/zsun/testfiles/data/bay_dem.tif\":Band")
	.value("format", "image/geotiff");

Message outm = sc.call(o);
		
sc.listOutputValues(outm);

```

An example code of calling WMS 1.3.0:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("http://cube.csiss.gmu.edu/geoserver/topp/ows?service=WMS&request=GetCapabilities&version=1.3.0", ServiceType.OGC)
	.build(); 

sc.listOperations();
		
Operation o = sc.operation("GetMap");
		
sc.listInputParams(o);

sc.listOutputParams(o);

o.getInput().value("layers", "topp:states")
	.value("bbox", "-124.73142200000001,24.955967,-66.969849,49.371735")
	.value("width", 768)
	.value("height", 330)
	.value("crs", "EPSG:4326")
	.value("format", "image/jpeg");

Message outm = sc.call(o);
		
sc.listOutputValues(outm);
```

An example code of calling WFS 2.0.0:

```
SUISClient sc = new SUISClient.Builder()
	.initialize("http://cube.csiss.gmu.edu/geoserver/topp/ows?service=WFS&request=GetCapabilities&version=2.0.0", ServiceType.OGC)
	.build(); 

sc.listOperations();
		
Operation o = sc.operation("GetFeature");
		
sc.listInputParams(o);

sc.listOutputParams(o);

o.getInput().value("query", "typeNames=topp:tasmania_roads");

Message outm = sc.call(o);
		
sc.listOutputValues(outm);
```

# Deployment

Import the suis4j library into your eclipse project classpath, or use Maven.

# Contributing


# Versioning


# Author

* **Ziheng Sun** - *Initial work* - [contact](https://zihengsun.com)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
