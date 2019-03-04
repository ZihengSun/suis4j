package suis4j.driver;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class AbstractDriver.java
*define all the common functions of a driver in SUIS architecture
*A driver should include the binding information from a profile to a specific server,
* and the functions to decode/encode SUIS messages and transform them to/from
* the messages compliant to services. 
*@author Ziheng Sun
*@time Dec 8, 2017 5:05:46 PM
*/
public abstract class AbstractDriver {
	
	URL access_endpoint, desc_endpoint;
	
	ServiceType servicetype;
	
	String current_operation;
	
	List<Operation> operlist;
	
	String id = UUID.randomUUID().toString(); //used to pair with Profile
	
	PayLoad response;
	
	public String getCurrent_operation() {
		return current_operation;
	}

	public void setCurrent_operation(String current_operation) {
		this.current_operation = current_operation;
	}

	public List<Operation> getOperlist() {
		return operlist;
	}

	public void setOperlist(List<Operation> operlist) {
		this.operlist = operlist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public URL getAccess_endpoint() {
		return access_endpoint;
	}

	public void setAccess_endpoint(URL access_endpoint) {
		this.access_endpoint = access_endpoint;
	}

	public URL getDesc_endpoint() {
		return desc_endpoint;
	}

	public void setDesc_endpoint(URL desc_endpoint) {
		this.desc_endpoint = desc_endpoint;
	}

	public ServiceType getServicetype() {
		return servicetype;
	}
	
	public void setServicetype(ServiceType servicetype) {
		this.servicetype = servicetype;
	}
	

	public Operation getOperation(String name){
		
		Operation o = null;
		
		for(int i=0;i<operlist.size();i++){
			
			if(operlist.get(i).getName().equals(name)){
				
				o = operlist.get(i);
				
				break;
				
			}
			
		}
		
		return o;
		
	}
	/**
	 * Connect operation to driver
	 */
	public void connect(){
		
		for(Operation o: this.getOperlist()){
			
			o.setDriverid(this.getId());
			
		}
		
	}
	
	/**
	 * Decode returned payload (received payload from client) to SUIS message object
	 * @param rawmsg
	 * @return
	 */
	abstract public Message decodeSUIS(Object rawmsg);
	
	/**
	 * Encode SUIS request message object to payload
	 * @param msg
	 * @return
	 */
	abstract public PayLoad encodeReq(Message msg);
	
	/**
	 * Send service-compliant request message to the actual web service
	 * @param req
	 */
	abstract public void send(PayLoad req);
	
	/**
	 * Fake send a request and get a fake response
	 * @param req
	 */
	public void fakesend(PayLoad req){
		
		throw new RuntimeException("Fakesend is not supported in this driver.");
		
	}
	
	/**
	 * Receive the response from actual web service
	 * @return
	 */
	abstract public PayLoad receive();
	
	/**
	 * Decode the response from actual web service into SUIS message object
	 * @param resp
	 * @return
	 */
	abstract public Message decodeResp(PayLoad resp);
	
	/**
	 * Encode the SUIS message object into transferable payload (SUIS raw message)
	 * @param msg
	 * @return
	 */
	abstract public Object encodeSUIS(Message msg);
	
	/**
	 * Digest the service description file
	 * @return
	 */
	abstract public List<Operation> digest();
	/**
	 * 
	 * @param name
	 * @return
	 */
	public void setCurrentOperation(String name){
		
		current_operation = name;
		
	}
	/**
	 * If the service params are not contained in the description file, 
	 * use this function to initialize the input and output parameters.
	 * @param o
	 */
	public abstract void initParams(Operation o);
	
	public static abstract class Builder {
		
		public abstract Builder parse(String descfile);
		
		public abstract Builder access_endpoint(URL url);
		
		public abstract Builder desc_endpoint(URL url);
		
		abstract public AbstractDriver build();

	}
	
}
