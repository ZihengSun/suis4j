# suis4j

SUIS (simple universal interface of service) java library

# Introduction

The disparate interfaces of web-based spatial information and analysis services cause very high barrier of entry. Simple Universal Interface of Service (SUIS) is invented to facilitate the use of heterogeneous web services in Earth science application. SUIS addresses to hide the technical details such as protocols, styles, bindings, schemas and addresses. An enhanced service-oriented framework is created to map the disparate interfaces into SUIS. 

A Java library is developed to support the automatic mapping from/to SOAP, OGC and REST services. SUIS aims to be extremely simple for entry-level consumers. With SUIS, users could be relieved from learning and communicating with all sorts of interfaces. Importantly, this work does not intend to replace the existing interfaces but supplement with an easier one to proxy them. 

A prototype excises SUIS and builds geoprocessing workflows over it.

# The underline principle of suis4j library:

The general service-call process of SUIS includes the foloowing basic steps:

1) create/read a suis request message

2) transform suis request message to service payload

3) send the service payload to service endpoint

4) receive the returned payload from service endpoint

5) transform response payload to suis response message

6) return suis message to display results

# Running

An example code of calling RESTful service:

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
