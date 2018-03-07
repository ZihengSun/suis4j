package suis4j.profile;

import suis4j.driver.AbstractDriver;
import suis4j.driver.AbstractDriverBuilder;
import suis4j.driver.DriverManager;
import suis4j.driver.OGCDriverBuilder;
import suis4j.driver.RESTDriverBuilder;
import suis4j.driver.SOAPDriverBuilder;
import suis4j.driver.ServiceType;

/**
*Class OperationBuilder.java
*@author Ziheng Sun
*@time Jan 31, 2018 11:06:14 AM
*/
public class OperationBuilder {

	Operation o;
	
	public OperationBuilder(){
		
		o = new Operation();
		
	}
	
	public OperationBuilder parse(String descfile, ServiceType type){
		
		AbstractDriverBuilder builder = null;
		
		switch(type){
			
			case OGC: builder = new OGCDriverBuilder(); break;
			
			case SOAP: builder = new SOAPDriverBuilder(); break;
			
			case REST: builder = new RESTDriverBuilder(); break;
			
		}
		
		AbstractDriver driver = builder.parse(descfile).build();
		
		DriverManager.add(driver);
		
//		o = driver.disgest();
		
		return this;
		
	}
	
	public OperationBuilder name(String n){
		
		o.setName(n);
		
		return this;
		
	}
	
	public OperationBuilder description(String desc){
		
		o.setDescription(desc);
		
		return this;
		
	}
	
	public OperationBuilder input(Message msg){
		
		o.setInput(msg);;
		
		return this;
		
	}
	
	public OperationBuilder output(Message msg){
		
		o.setOutput(msg);
		
		return this;
		
	}
	
	public OperationBuilder driver(String did){
		
		o.setDriverid(did);
		
		return this;
		
	}
	
	public Operation build(){
		
		return o;
		
	}
	
}
