package suis4j.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;

import suis4j.driver.DriverManager;
import suis4j.driver.ServiceType;
import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;
import suis4j.profile.Operation;
import suis4j.profile.OperationBuilder;

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
	
	public Message call(String operationame, Message input){
		
		System.out.println("Call the web service..");
		
		DriverManager.add(null);
		
		
		return null;
		
	}
	
	public void vis(Message m){
		
		System.out.println("Begin to visualize the parameters in the message..");
		
	}
	
	public static void main(String[] args) {
		
		SUISClient sc = new SUISClientBuilder()
				.register("http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_GetProjection?wsdl", ServiceType.SOAP)
				.build();
		
		System.out.println("The client object is created..");
		
		Operation o = sc.getOpers().get(sc.getOpers().size()-1);
		
		Message inm = o.getInput();
		
		Message outm = sc.call(o.getName(), inm);
		
		sc.vis(outm);
		
		System.exit(0);
		
	}

}
