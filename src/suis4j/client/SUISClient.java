package suis4j.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import suis4j.driver.AbstractDriver;
import suis4j.driver.DriverManager;
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
	
	Logger log = Logger.getLogger(SUISClient.class);
	
	List<Operation> opers = new ArrayList();
	
	public List<Operation> getOpers() {
		
		return opers;
	
	}

	public void setOpers(List<Operation> opers) {
		
		this.opers = opers;
	
	}

	protected SUISClient(){
		
		
		
	}
	
	public List<Operation> search(){
		
		return null;
		
	}
	
	public void remove(Operation o){
		
		
		
	}
	
	public void listOperations(){
		
		for(Operation o : opers){
			
			System.out.println("Operation: " + o.getName());
			
		}
		
	}
	
	public void listInputs(Operation oper){
		
		System.out.println("Inputs include: ");
		
		for(Parameter p : oper.getInput().getParameter_list()){
			
			System.out.println("parameter - " + p.getName());
			
		}
		
	}
	
	public void listOutputs(Operation oper){
		
		System.out.println("Outputs include: ");
		
		for(Parameter p : oper.getOutput().getParameter_list()){
			
			System.out.println("parameter - " + p.getName());
			
		}
		
	}
	
	public Message call(Operation o, Message input){
		
		System.out.println("Call the web service..");
		
		String driverid = o.getDriverid();
		
		AbstractDriver ad = DriverManager.get(driverid);
		
		ad.send(ad.encodeReq(input));
		
		return ad.decodeResp(ad.receive());
		
	}
	
	public void visualize(Message m){
		
		System.out.println("Begin to visualize the FILE parameters in the message..");
		
	}
	
	public static void main(String[] args) {
		
		try{
			
			System.out.println("Start the demo of SUIS client..");
			
			SUISClient sc = new SUISClientBuilder()
					
					//.register("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_XYOffset?wsdl", ServiceType.SOAP)
//					.register("http://eds-mobile.com/eds.wsdl", ServiceType.SOAP)
					.register("http://queue.amazonaws.com/doc/2009-02-01/QueueService.wsdl", ServiceType.SOAP)
					
					.build();
			
			sc.listOperations();
			
			Operation o = sc.getOpers().get(sc.getOpers().size()-1);
			
			sc.listInputs(o);
			
			sc.listOutputs(o);
			
			Message inm = o.getInput()
					
//					.value("sourceURL", "http://test.com/test.zip")
//					
//					.value("xoffset", 1.0)
//					
//					.value("yoffset", 1.0)
					;
			
			Message outm = sc.call(o, inm);
			
			sc.visualize(outm);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			System.exit(0);
			
		}
		
	}

}
