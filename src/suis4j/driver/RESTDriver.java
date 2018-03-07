package suis4j.driver;

import java.util.List;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class REST2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:31 AM
*/
public class RESTDriver extends AbstractDriver {

	@Override
	public AbstractRequestBuilder getReqbuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractResponseParser getRespparser() {
		// TODO Auto-generated method stub
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
		
		return null;
	}
	
	
	
}
