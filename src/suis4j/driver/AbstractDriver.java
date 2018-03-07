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
	
	AbstractRequestBuilder reqbuilder;
	
	AbstractResponseParser respparser;
	
	String id = UUID.randomUUID().toString(); //used to pair with Profile
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReqbuilder(AbstractRequestBuilder reqbuilder) {
		this.reqbuilder = reqbuilder;
	}

	public void setRespparser(AbstractResponseParser respparser) {
		this.respparser = respparser;
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
	
	/**
	 * Each driver should equip with a request builder and a response builder
	 * @return
	 */
	abstract public AbstractRequestBuilder getReqbuilder();
	
	abstract public AbstractResponseParser getRespparser();
	
	/**
	 * Decode SUIS raw message (received payload from client) to SUIS message object
	 * @param rawmsg
	 * @return
	 */
	abstract public Message decodeSUIS(Object rawmsg);
	/**
	 * Encode SUIS request message object to service-compliant request message
	 * @param msg
	 * @return
	 */
	abstract public Object encodeReq(Message msg);
	/**
	 * Send service-compliant request message to the actual web service
	 * @param req
	 */
	abstract public void send(Object req);
	/**
	 * Receive the response from actual web service
	 * @return
	 */
	abstract public Object receive();
	/**
	 * Decode the response from actual web service into SUIS message object
	 * @param resp
	 * @return
	 */
	abstract public Message decodeResp(Object resp);
	/**
	 * Encode the SUIS message object into transferable payload (SUIS raw message)
	 * @param msg
	 * @return
	 */
	abstract public Object encodeSUIS(Message msg);
	
	
	abstract public List<Operation> digest();
	
	
}
