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
					.initialize("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_XYOffset?wsdl", ServiceType.SOAP)
//					.initialize("http://eds-mobile.com/eds.wsdl", ServiceType.SOAP)
//					.initialize("http://queue.amazonaws.com/doc/2009-02-01/QueueService.wsdl", ServiceType.SOAP)
//					.initialize("http://cube.csiss.gmu.edu/cgi-bin/wcs-all.cgi?service=WCS&version=2.0.0&request=GetCapabilities", ServiceType.OGC)
//					.initialize("https://raw.githubusercontent.com/jonathanrobie/restful-service-description-language/master/examples/maps.rsdl", ServiceType.REST)
					.build();
			
			sc.listOperations();
			
			Operation o = sc.operation(0);
			
			sc.listInputs(o);
			
			sc.listOutputs(o);
			
			Message inm = o.getInput()
					.value("sourceURL", "http://test.com/test.zip")
					.value("xoffset", 1.0)
					.value("yoffset", 1.0);
			
			Message outm = sc.call(o, inm);
			
			sc.visualize(outm);
			
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
