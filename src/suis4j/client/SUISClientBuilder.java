package suis4j.client;

import java.util.List;

import org.apache.log4j.Logger;

import suis4j.driver.AbstractDriver;
import suis4j.driver.AbstractDriverBuilder;
import suis4j.driver.DriverManager;
import suis4j.driver.OGCDriverBuilder;
import suis4j.driver.RESTDriverBuilder;
import suis4j.driver.SOAPDriverBuilder;
import suis4j.driver.ServiceType;
import suis4j.profile.Operation;

/**
*Class SUISClientBuilder.java
*@author Ziheng Sun
*@time Feb 14, 2018 12:40:21 PM
*/
public class SUISClientBuilder {

	Logger log = Logger.getLogger(this.getClass());
	
	SUISClient c = new SUISClient();
	
	public SUISClientBuilder register(String descfile, ServiceType type){
		
		log.info("register new wsdl...");
		
		AbstractDriverBuilder builder = null;
		
		switch(type){
		
			case SOAP: builder = new SOAPDriverBuilder();break;
			
			case OGC: builder = new OGCDriverBuilder();break;
			
			case REST: builder = new RESTDriverBuilder();break;
		
		}
		
		AbstractDriver ad =  builder.parse(descfile)
				.build(); //build a driver
		
		DriverManager.add(ad);
		
		List<Operation> os = ad.digest(); //digest the description file and export a list of operations
		
		for(Operation o: os){
			
			this.register(o);	
			
		}
		
		return this;
		
	}
	
	public SUISClientBuilder register(Operation o){
		
		c.getOpers().add(o);
		
		return this;
		
	}
	
	public SUISClientBuilder load(){
		
		
		
		return this;
		
	}
	
	public SUISClient build(){
		
		System.out.println("The client object is created..");
		
		return c;
		
	}
	
}
