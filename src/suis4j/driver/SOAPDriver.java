package suis4j.driver;

import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.OperationBuilder;

/**
*Class WSDL2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:03 AM
*/
public class SOAPDriver extends AbstractDriver {
	
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
	public Operation disgest() {
		
		Operation o = new OperationBuilder().build();
		
		return o;
		
	}
	
}
