package suis4j.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.model.iface.MessagePart;

import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;
import suis4j.profile.Operation;
import suis4j.profile.OperationBuilder;
import suis4j.profile.Parameter;
import suis4j.profile.ParameterBuilder;

/**
*Class WSDL2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:03 AM
*/
public class SOAPDriver extends AbstractDriver {
	
	WsdlInterface iface = null;
	
	public WsdlInterface getIface() {
		return iface;
	}

	public void setIface(WsdlInterface iface) {
		this.iface = iface;
	}

	@Override
	public AbstractRequestBuilder getReqbuilder() {
		
		return null;
	}

	@Override
	public AbstractResponseParser getRespparser() {

		return null;
	}

	@Override
	public Message decodeSUIS(Object rawmsg) {
		
		return null;
	}

	@Override
	public Object encodeReq(Message msg) {
		
		return null;
	}

	@Override
	public void send(Object req) {
		
	}

	@Override
	public Object receive() {
		
		return null;
	}

	@Override
	public Message decodeResp(Object resp) {
		
		return null;
	}

	@Override
	public Object encodeSUIS(Message msg) {
		
		return null;
	}

	@Override
	public List<Operation> digest() {
		
		List<Operation> os = new ArrayList();
		
		for(int i=0;i<iface.getAllOperations().length;i++){
        	
        	com.eviware.soapui.model.iface.Operation oper = iface.getAllOperations()[i];
        	
        	MessagePart mp = oper.getDefaultRequestParts()[0];
        	
        	System.out.println("Operation : " + oper.getName());
        	
        	System.out.println("has "+oper.getDefaultRequestParts().length + " requests");
        	
        	System.out.println("part: "+mp.getName());
        	
        	System.out.println("has : "+oper.getDefaultResponseParts().length + " responses");
        	
        	System.out.println("has "+mp.getName() + " requests");
        	
        	System.out.println("part: "+mp.getName());
        	
        	System.out.println("soap version: " + iface.getSoapVersion());
        	
        	String req = oper.createRequest( false );
        	
        	System.out.println("Request String Content: " + req);
        	
        	//get parameters from the generated request
        	
        	String resp = oper.createResponse(false);
        	
        	System.out.println("Response String Content: " + resp);
        	
        	//get parameters from the generated response
        	
        	//create an operation object
        	
        	List<Parameter> inparams = new ArrayList();
        	
        	for(int j = 0; j<oper.getDefaultRequestParts().length; j++){
        		
        		MessagePart part = oper.getDefaultRequestParts()[j];
        		
        		Parameter p = new ParameterBuilder()
        		
        				.name(part.getName())
        				
        				.description(part.getDescription())
        				
        				.build();
        		
        		inparams.add(p);
        		
        	}
        	
        	Message in = new MessageBuilder()
        			
        			.params(inparams)
        			
        			.build();
        	
        	List<Parameter> outparams = new ArrayList();
        	
        	for(int j=0; j<oper.getDefaultResponseParts().length; j++){
        		
        		MessagePart part = oper.getDefaultResponseParts()[j];
        		
        		Parameter p = new ParameterBuilder()
        				
        				.name(part.getName())
        				
        				.description(part.getDescription())
        				
        				.build();
        		
        		outparams.add(p);
        		
        	}
        	
        	Message out = new MessageBuilder()
        			
        			.params(outparams)
        			
        			.build();
        	
        	Operation o = new OperationBuilder()
        			
        			.name(oper.getName())
        			
        			.description(oper.getDescription())
        			
        			.driver(this.getId())
        			
        			.input(in)
        			
        			.output(out)
        			
        			.build();
        	
    		os.add(o);
        	
        }
		
		return os;
		
	}
	
}
