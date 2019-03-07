package suis4j.client;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import suis4j.driver.AbstractDriver;
import suis4j.driver.DriverManager;
import suis4j.driver.HttpUtils;
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
	
	public String doHTTPPost(String request, String url){
		
		String resp = null;
		
		try {
			
			resp = HttpUtils.doPost(url, request);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return resp;
		
	}
	
	public String doHTTPGet(String url){
		
		String resp = null;
		
		try {
			
			resp = HttpUtils.doGet(url);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return resp;
		
	}
	
	/**
	 * This function enables users to upload a file and get a URL for it
	 * @param filepath
	 * @param url
	 */
	public void uploadFile(String filepath, URL url){
		
		
		
	}
	
	/**
	 * This function provide an entry for directly downloading from URL
	 * It is used when users are aware of the URL and just want to get the file in the program. 
	 * @param url
	 * @param filepath
	 */
	public void downloadURL(URL url, String filepath){
		
		try {
			
			HttpUtils.doGETFile(url.toString(), filepath);
			
			log.info("File is downloaded: " + filepath);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to download the file. ");
			
		}
		
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
		
		log.info("There are totally " + driver.getOperlist().size() + " operations");
		
		for(Operation oper : driver.getOperlist()){
			
			log.info("operation " + oper.getName());
			
		}
		
	}
	
	
	public void listInputParams(Operation oper){
		
		AbstractDriver ad = DriverManager.get(oper.getDriverid());
		
		ad.initParams(oper);
		
		log.info("Inputs include: ");
		
		for(Parameter p : oper.getInput().getParameter_list()){
			
			log.info("parameter - " + p.getName());
			
		}
		
	}
	
	public void listOutputParams(Operation oper){
		
		AbstractDriver ad = DriverManager.get(oper.getDriverid());
		
		ad.initParams(oper);
		
		log.info("Outputs include: ");
		
		for(Parameter p : oper.getOutput().getParameter_list()){
			
			log.info("parameter - " + p.getName());
			
		}
		
	}
	
	public Message fakecall(Operation o){

		log.info("Call the web service..");
		
		String driverid = o.getDriverid();
		
		AbstractDriver ad = DriverManager.get(driverid);
		
		ad.setCurrentOperation(o.getName());
		
		ad.fakesend(ad.encodeReq(o.getInput()));
		
		return ad.decodeResp(ad.receive());
		
	}
	
	public Message call(Operation o){
		
		log.info("Call the web service..");
		
		String driverid = o.getDriverid();
		
		AbstractDriver ad = DriverManager.get(driverid);
		
		ad.setCurrentOperation(o.getName());
		
		ad.send(ad.encodeReq(o.getInput()));
		
		return ad.decodeResp(ad.receive());
		
	}
	
	public void listOutputValues(Message m){
		
		m.listKVPs();
		
	}
	
	public void visualize(Message m){
		
		log.info("Begin to visualize the FILE parameters in the message..");
		
		if(m.getError()!=null){
			
			System.err.println("Get Error Response: " + m.getError());
			
		}else{
			
			log.info("Get Correct Response");
			
			log.info("save the result to file");
			
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
			
			log.debug("recognizing new service...");
			
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
