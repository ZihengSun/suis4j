package suis4j;

import org.junit.Test;

import suis4j.client.SUISClient;
import suis4j.driver.ServiceType;
import suis4j.profile.Operation;

/**
*Class FVCOMTest.java
*@author Ziheng Sun
*@time Apr 27, 2018 7:20:03 PM
*/
public class FVCOMTest {
	
//	@Test
	public void testFVCOM(){
		
		try{
			
			SUISClient sc = new SUISClient.Builder().initialize("http://cube.csiss.gmu.edu/CubeModelWS/services/FVCOM?wsdl", ServiceType.SOAP).build();
			
			Operation gethycom = sc.operation("Generate_HYCOM_links");
			
			gethycom.getInput().value("year", "2009")
				.value("day","091")
				.value("hour", "19");
			
			sc.call(gethycom);
			
			System.out.println(gethycom.getOutput().value("hycom_combination_file_url"));
			
			Operation interpolate = sc.operation("interpolate_HYCOM_combine_to_FVCOM_grid");
			
			interpolate.getInput().value("hycom_combine_InputURL", (String) gethycom.getOutput().value("hycom_combination_file_url"));
			
			sc.call(interpolate);
			
			System.out.println(interpolate.getOutput().value("inter_temperature_InputURL"));
			
			Operation rewrite = sc.operation("rewrite_to_FVCOM_input_format");
			
			rewrite.getInput().value("inter_temperature_InputURL", (String)interpolate.getOutput().value("inter_temperature_InputURL"))
				.value("inter_salinity_InputURL", (String)interpolate.getOutput().value("inter_salinity_InputURL"))
				.value("inter_U_velocity_InputURL", (String)interpolate.getOutput().value("inter_U_velocity_InputURL"))
				.value("inter_V_velocity_InputURL", (String)interpolate.getOutput().value("inter_V_velocity_InputURL"));
			
			sc.call(rewrite);
			
			System.out.println("output url: " + rewrite.getOutput().value("returnURL"));
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}


}
