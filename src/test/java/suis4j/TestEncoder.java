package suis4j;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import suis4j.driver.AbstractDriver;
import suis4j.driver.DriverManager;
import suis4j.driver.PayLoad;
import suis4j.driver.SOAPDriver;
import suis4j.driver.SUISDriver;
import suis4j.driver.ServiceType;
import suis4j.profile.Message;
import suis4j.profile.Parameter;

/**
*Class TestEncoder.java
*@author Ziheng Sun
*@time Jan 31, 2018 3:43:55 PM
*/
public class TestEncoder {
	
	@Test
	public void encode_decode(String[] args) {
		
		AbstractDriver driver = DriverManager.create(ServiceType.SOAP);
		
		ObjectMapper om = new ObjectMapper();
		
		try {
			
			String driverstr = om.writeValueAsString(driver);
			
			System.out.println(driverstr);
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		}
		
		List<Parameter> paramlist = new ArrayList();
		
		Message msg = new Message.Builder()
		
				.params(paramlist)
				
				.build();
		
		Object suismsg = driver.encodeSUIS(msg);
		
		System.out.println(suismsg);
		
		Message newmsg = driver.decodeSUIS(suismsg);
		
	}

}
