package suis4j;

import java.util.UUID;

import org.junit.Test;

import suis4j.client.SUISClient;
import suis4j.client.SUISClient.Builder;
import suis4j.driver.HttpUtils;
import suis4j.driver.ServiceType;
import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class Main.java
*@author Ziheng Sun
*@time Mar 12, 2018 11:03:36 AM
*/
public class Main {
	
//	@Test
	public void polygonClip(){
		
		try{
			
			SUISClient sc = new SUISClient.Builder().initialize("http://cube.csiss.gmu.edu/axis/services/Raster_PolygonClip?wsdl", ServiceType.SOAP).build();
			
//			sc.uploadFile("E://TESTDATA/drought/drought.2017.289.california.3857.tif", 
//					new URL("http://cube.csiss.gmu.edu/CyberConnector/FileUploadServlet"));
			
			Operation o = sc.operation("polygon_clip");
			
			sc.listInputParams(o);
			sc.listOutputParams(o);
			
			o.getInput().value("sourceURL","http://cube.csiss.gmu.edu/GEOPORTAL_DATA_CACHE/data/testdrought.tif")
				.value("polygonURL", "http://cube.csiss.gmu.edu/GEOPORTAL_DATA_CACHE/data/california.gml");
			
			sc.call(o);
			
			String returnURL = o.getOutput().getValueAsString("returnURL");
			
			System.out.println("returned file URL is : " + returnURL);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
	@Test
	public void suisQOS() {
		
		for(int i=0;i<10;i++) {
			
			testDroughtWorkflow();
			
		}
		
	}
	
	public void testDroughtWorkflow(){
		
		try{
			
//			System.out.println("Start the workflow of agricultural drought.");
			
			long startTime = System.currentTimeMillis();
			
			//step 1: get the drought raster from GADMFS
			
			Builder clientBuilder = new SUISClient.Builder();
			
			long step0cost = System.currentTimeMillis();
			
			SUISClient sc = clientBuilder
					.initialize("http://gis.csiss.gmu.edu/cgi-bin/mapserv?MAP=/media/gisiv01/mapfiles/drought/16days/2018/drought.2018.113.map&SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCapabilities", ServiceType.OGC).build();
			
			long step1cost = System.currentTimeMillis();
			
//			System.out.println("step 1 takes " + (step1cost) + " ms");
			
			sc.listOperations();
			
			Operation o = sc.operation("GetCoverage");
			
			long step2cost = System.currentTimeMillis();
			
//			System.out.println("step 2 takes " + (step2cost) + " ms");
			
			sc.listInputParams(o);
			
			long step3cost = System.currentTimeMillis();
			
//			System.out.println("step 3 takes " + (step3cost) + " ms");
			
			o.getInput().value("format", "image/tiff")
				.value("coverage","drought.2018.113")
				//download california
				.value("bbox", "5938055.68297,2798034.270687,12338055.68297,9198034.270687")
				.value("width", 1000)
				.value("height", 1000)
				.value("crs", "epsg:900101");
			
			long step4cost = System.currentTimeMillis();
			
//			System.out.println("step 4 takes " + (step4cost) + " ms");
			
			Message droughtraster = sc.call(o);
			
			long step5cost = System.currentTimeMillis();
			
//			System.out.println("step 5 takes " + (step5cost) + " ms");
			
			String vcifilepath = o.getOutput().getValueAsString("coverage");
			
			String vciurl = o.getOutput().getValueAsString("dataurl");
			
			long step6cost = System.currentTimeMillis();
			
			long endTime = System.currentTimeMillis();
			
			double seconds = (endTime - startTime) ;
			
			System.out.println( (step0cost-startTime) + ", " + (step1cost-step0cost) + ", " + (step2cost-step1cost) + ", " + (step3cost-step2cost) + ", " + (step4cost-step3cost) + ", " + (step5cost-step4cost) + ", " + (step6cost-step5cost));
			
			
//			System.out.println("step 6 takes " + (endTime) +" ms");
//			
//			System.out.println("VCI file path : " + vcifilepath);
//			
//			System.out.println("this service call costs " + seconds + " seconds");
			
//			129.174.131.10/cgi-bin/mapserv?SRS=EPSG:102004&LAYERS=drought.2017.289&MAP=/media/gisiv01/mapfiles/drought/16days/2017/drought.2017.289.map&SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCoverage&identifier=drought.2017.289&BBOX=-124.79,42.11,-113.83,82.11&WIDTH=500&HEIGHT=500&FORMAT=image/tiff
			
			//step 2: get the precipitation from NWS
			
			String precipfilepath = HttpUtils.TEMPORARY_PATH + "precip-" + UUID.randomUUID().toString() + ".tiff";
			
			String precipurl = "https://water.weather.gov/precip/downloader.php?date=20180101&file_type=geotiff&range=year2date&format=tar";
			
//			sc.downloadURL(new URL(precipurl), precipfilepath);
			
			
			
			//step 3: get california boundary from WFS
			
			
			
//			//step 4: reproject the drought and precipitation into EPSG:3857
//			
//			sc = new SUISClient.Builder()
//					.initialize("http://geoprocessing.demo.52north.org/latest-wps/WebProcessingService?Request=GetCapabilities&Service=WPS&version=1.0.0", ServiceType.OGC).build();
//			
//			o = sc.operation("r.proj");
//			
//			sc.listInputParams(o);
//			sc.listOutputParams(o);
			
			//step 4: reproject the precipitation to the drought projection
			
			
			
			//step 5: calculate new index of the drought and precipitation
			// drought index = vci*0.5 + precip*0.5
			
			
			
//			System.out.println("End of the workflow.");
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
//	@Test
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
//					.initialize("http://cube.csiss.gmu.edu/geoserver/topp/ows?service=WFS&request=GetCapabilities&version=2.0.0", ServiceType.OGC) //for WFS 1.0.0 test
					//WPS
//					.initialize("http://cube.csiss.gmu.edu/cgi-bin/pywps.cgi?service=WPS&request=GetCapabilities&version=1.0.0", ServiceType.OGC) //for WPS 1.0.0 test
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
			
			Operation o = sc.operation("GetCoverage");
			
			sc.listInputParams(o);
			
			sc.listOutputParams(o);
			
			//for SOAP test
//			o.getInput()
//					.value("sourceURL", "http://www3.csiss.gmu.edu/data/building.zip")
//					.value("buffer", 100);
			
			//for REST test
//			o.getInput()
//					.value("network", "IU")
//					.value("station", "ANMO")
//					.value("location", "00")
//					.value("channel", "BHZ")
//					.value("starttime", "2001-12-09T12:00:00")
//					.value("endtime", "2001-12-09T12:20:00")
////					.value("duration", "30")
//					.value("output", "plot");
			
			//for WPS test
//			o.getInput().value("data", "http://geoprocessing.demo.52north.org:8080/geoserver/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=topp:tasmania_roads&SRS=EPSG:4326&OUTPUTFORMAT=GML3")
//				.value("width", 0.05);
			
			//for OGC WCS test
			o.getInput().value("coverageId", "GEOTIFF:\"/home/zsun/testfiles/data/bay_dem.tif\":Band").value("format", "image/geotiff");
			o.getInput().value("coverageId", "atmosphere__METOP-B_GOME-2_L3_TROPOSPHERIC_O3_MIXINGRATIO_STD");
			
			//for OGC WFS test
//			o.getInput().value("query", "typeNames=topp:tasmania_roads");
//			o.getInput().value("typeName", "topp:tasmania_roads");
			
			//for OGC WMS test
//			o.getInput().value("layers", "topp:states");
//			o.getInput().value("bbox", "-124.73142200000001,24.955967,-66.969849,49.371735");
//			o.getInput().value("width", 768);
//			o.getInput().value("height", 330);
//			o.getInput().value("crs", "EPSG:4326");
//			o.getInput().value("format", "image/jpeg");
			
			//for OGC CSW test
			//missing
			
			Message outm = sc.call(o);
			
			sc.listOutputValues(outm);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}finally{
			
			System.exit(0);
			
		}
		
	}
	
//	public static void main(String[] args) {
//		
//		Main m = new Main();
//		
//		m.testSUIS4J();
//		
////		for(int i=0;i<50;i++){
//		
////			m.testDroughtWorkflow();
//			
////		}
//		
////		m.polygonClip();
//		
//		System.exit(0);
//		
//	}
	
}
