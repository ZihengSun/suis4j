package suis4j.driver;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class SUNISMapper.java
*@author Ziheng Sun
*@time Dec 8, 2017 5:05:46 PM
*/
public abstract class AbstractDriver {
	
	String endpoint, servicetype, protocol;
	
	AbstractRequestBuilder reqbuilder;
	
	AbstractResponseParser respparser;
	
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public AbstractRequestBuilder getReqbuilder() {
		return reqbuilder;
	}
	public void setReqbuilder(AbstractRequestBuilder reqbuilder) {
		this.reqbuilder = reqbuilder;
	}
	public AbstractResponseParser getRespparser() {
		return respparser;
	}
	public void setRespparser(AbstractResponseParser respparser) {
		this.respparser = respparser;
	}
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
	
	
	abstract public Operation disgest();
	
	
}
