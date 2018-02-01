package suis4j.driver;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class SUNISMapper.java
*@author Ziheng Sun
*@time Dec 8, 2017 5:05:46 PM
*/
public interface AbstractDriver {
	/**
	 * Decode SUIS raw message (received payload from client) to SUIS message object
	 * @param rawmsg
	 * @return
	 */
	public Message decodeSUIS(Object rawmsg);
	/**
	 * Encode SUIS request message object to service-compliant request message
	 * @param msg
	 * @return
	 */
	public Object encodeReq(Message msg);
	/**
	 * Send service-compliant request message to the actual web service
	 * @param req
	 */
	public void send(Object req);
	/**
	 * Receive the response from actual web service
	 * @return
	 */
	public Object receive();
	/**
	 * Decode the response from actual web service into SUIS message object
	 * @param resp
	 * @return
	 */
	public Message decodeResp(Object resp);
	/**
	 * Encode the SUIS message object into transferable payload (SUIS raw message)
	 * @param msg
	 * @return
	 */
	public Object encodeSUIS(Message msg);
	
	
}
