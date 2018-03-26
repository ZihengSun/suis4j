package suis4j.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import suis4j.driver.AbstractDriver;
import suis4j.driver.DriverManager;
import suis4j.driver.OGCDriver;
import suis4j.driver.RESTDriver;
import suis4j.driver.SOAPDriver;
import suis4j.driver.ServiceType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class SUISClient.java
*@author Ziheng Sun
*@time Feb 1, 2018 3:20:39 PM
*/
public class SUISClient {
	
	Logger log = Logger.getLogger(this.getClass());
	
	AbstractDriver driver;
	
	public AbstractDriver getDriver() {
		return driver;
	}

	public void setDriver(AbstractDriver driver) {
		this.driver = driver;
	}

	protected SUISClient(){
		
	}
	
	public Operation operation(String name){
		
		Operation o = null;
		
		for(Operation theo : this.getDriver().getOperlist()){
			
			if(name.equals(theo.getName())){
				
				o = theo;
				
				break;
				
			}
			
		}
		
		return o;
		
	}
	
	public Operation operation(int index){
		
		return this.getDriver().getOperlist().get(index);
		
	}
	
	public void listOperations(){
		
		System.out.println("There are totally " + driver.getOperlist().size() + " operations");
		
		for(Operation oper : driver.getOperlist()){
			
			System.out.println("operation " + oper.getName());
			
		}
		
	}
	
	
	public void listInputs(Operation oper){
		
		AbstractDriver ad = DriverManager.get(oper.getDriverid());
		
		ad.initParams(oper);
		
		System.out.println("Inputs include: ");
		
		for(Parameter p : oper.getInput().getParameter_list()){
			
			System.out.println("parameter - " + p.getName());
			
		}
		
	}
	
	public void listOutputs(Operation oper){
		
		AbstractDriver ad = DriverManager.get(oper.getDriverid());
		
		ad.initParams(oper);
		
		System.out.println("Outputs include: ");
		
		for(Parameter p : oper.getOutput().getParameter_list()){
			
			System.out.println("parameter - " + p.getName());
			
		}
		
	}
	
	public Message call(Operation o){
		
		log.info("Call the web service..");
		
		String driverid = o.getDriverid();
		
		AbstractDriver ad = DriverManager.get(driverid);
		
		ad.setCurrentOperation(o.getName());
		
		ad.send(ad.encodeReq(o.getInput()));
		
		return ad.decodeResp(ad.receive());
		
	}
	
	public void listOutput(Message m){
		
		m.listKVPs();
		
	}
	
	public void visualize(Message m){
		
		log.info("Begin to visualize the FILE parameters in the message..");
		
		if(m.getError()!=null){
			
			System.err.println("Get Error Response: " + m.getError());
			
		}else{
			
			System.out.println("Get Correct Response");
			
			System.out.println("save the result to file");
			
//			m.get("return").getValue();
			
//			m.listKVPs();
			
			
		}
		
	}
	
	public static class Builder {
		
		Logger log = Logger.getLogger(this.getClass());
		
		SUISClient c = new SUISClient();
		
		/**
		 * Parse all the operations into this client
		 * @param descfile
		 * @param type
		 * @return
		 */
		public Builder initialize(String descfile, ServiceType type){
			
			log.debug("register new wsdl...");
			
			AbstractDriver.Builder builder = null;
			
			switch(type){
			
				case SOAP: builder = new SOAPDriver.Builder();break;
				
				case OGC: builder = new OGCDriver.Builder();break;
				
				case REST: builder = new RESTDriver.Builder();break;
			
			}
			
			AbstractDriver ad =  builder.parse(descfile).build(); //build a driver
			
			DriverManager.add(ad);
			
			ad.digest(); //digest the description file and export a list of operations
			
			c.setDriver(ad);
			
			return this;
			
		}
		
		public SUISClient build(){
			
			log.info("The client object is created..");
			
			return c;
			
		}
		
	}

}
