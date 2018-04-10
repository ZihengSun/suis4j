package suis4j.client;

import suis4j.driver.ServiceType;
import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class Main.java
*@author Ziheng Sun
*@time Mar 12, 2018 11:03:36 AM
*/
public class Main {

	public void testSUIS4J(){

		try{

			System.out.println("Start the demo of SUIS client..");

			SUISClient sc = new SUISClient.Builder()
					//Every client corresponds to only a service. To call multiple services, create multiple clients.
					//SOAP
//					.initialize("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_Buffer_OGR?wsdl", ServiceType.SOAP)
//					.initialize("http://cube.csiss.gmu.edu/axis/services/Vector_GML2SHP?wsdl", ServiceType.SOAP)
//					.initialize("http://www3.csiss.gmu.edu/axis2/services/GMU_SOAP_WCS_Service?wsdl", ServiceType.SOAP)
//					.initialize("http://eds-mobile.com/eds.wsdl", ServiceType.SOAP)
//					.initialize("http://queue.amazonaws.com/doc/2009-02-01/QueueService.wsdl", ServiceType.SOAP)
					//REST-WADL
//					.initialize("https://service.iris.edu/irisws/timeseries/1/application.wadl", ServiceType.REST)
					//WCS
					.initialize("http://ows9.csiss.gmu.edu/cgi-bin/WCS20-r?service=WCS&version=2.0.0&request=GetCapabilities", ServiceType.OGC)
//					.initialize("http://earthserver.ecmwf.int/rasdaman/ows?service=WCS&request=GetCapabilities&version=2.0.1", ServiceType.OGC)
//					.initialize("https://geoservice.dlr.de/eoc/atmosphere/wcs?SERVICE=WCS&REQUEST=GetCapabilities&version=2.0.0", ServiceType.OGC)
					//WFS
//					.initialize("http://cube.csiss.gmu.edu/geoserver/topp/ows?service=WFS&request=GetCapabilities&version=2.0.0", ServiceType.OGC) //for WFS 1.0.0 tes
					//WPS
//					.initialize("http://cube.csiss.gmu.edu/cgi-bin/pywps.cgi?service=WPS&request=GetCapabilities&version=1.0.0", ServiceType.OGC) //for WPS 1.0.0 tes
//					.initialize("http://geoprocessing.demo.52north.org/latest-wps/WebProcessingService?Request=GetCapabilities&Service=WPS&version=1.0.0", ServiceType.OGC)
//					.initialize("http://wps.statistical.d4science.org/wps/WebProcessingService?service=WPS&Request=GetCapabilities&version=1.0.0", ServiceType.OGC)
					//REST-RSDL
//					.initialize("https://raw.githubusercontent.com/jonathanrobie/restful-service-description-language/master/examples/maps.rsdl", ServiceType.REST) //for RSDL, but is not support at this moment.
					//WMS
//					.initialize("http://cube.csiss.gmu.edu/geoserver/topp/ows?service=WMS&request=GetCapabilities&version=1.3.0", ServiceType.OGC)
					//CSW
//					.initialize("http://cube.csiss.gmu.edu/srv/csw?service=CSW&request=GetCapabilities&version=2.0.2", ServiceType.OGC)
					.build();

			sc.listOperations();

			Operation o = sc.operation("GetMap");

			sc.listInputParams(o);

			sc.listOutputParams(o);

			//for SOAP tes
//			o.getInput()
//					.value("sourceURL", "http://www3.csiss.gmu.edu/data/building.zip")
//					.value("buffer", 100);

			//for REST tes
//			o.getInput()
//					.value("network", "IU")
//					.value("station", "ANMO")
//					.value("location", "00")
//					.value("channel", "BHZ")
//					.value("starttime", "2001-12-09T12:00:00")
//					.value("endtime", "2001-12-09T12:20:00")
////					.value("duration", "30")
//					.value("output", "plot");

			//for WPS tes
//			o.getInput().value("data", "http://geoprocessing.demo.52north.org:8080/geoserver/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=topp:tasmania_roads&SRS=EPSG:4326&OUTPUTFORMAT=GML3")
//				.value("width", 0.05);

			//for OGC WCS tes
//			o.getInput().value("coverageId", "GEOTIFF:\"/home/zsun/testfiles/data/bay_dem.tif\":Band").value("format", "image/geotiff");
//			o.getInput().value("coverageId", "atmosphere__METOP-B_GOME-2_L3_TROPOSPHERIC_O3_MIXINGRATIO_STD");

			//for OGC WFS tes
//			o.getInput().value("query", "typeNames=topp:tasmania_roads");
//			o.getInput().value("typeName", "topp:tasmania_roads");

			//for OGC WMS tes
			o.getInput().value("layers", "topp:states");
			o.getInput().value("bbox", "-124.73142200000001,24.955967,-66.969849,49.371735");
			o.getInput().value("width", 768);
			o.getInput().value("height", 330);
			o.getInput().value("crs", "EPSG:4326");
			o.getInput().value("format", "image/jpeg");

			Message outm = sc.call(o);

			sc.listOutputValues(outm);

		}catch(Exception e){

			e.printStackTrace();

		}finally{

			System.exit(0);

		}

	}

	public static void main(String[] args) {

		Main m = new Main();

		m.testSUIS4J();

	}

}
