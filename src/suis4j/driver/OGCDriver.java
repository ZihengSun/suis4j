package suis4j.driver;

import java.util.List;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class OGCWPS2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:52 AM
*/
public class OGCDriver extends AbstractDriver {

	@Override
	public Object encodeReq(Message msg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void send(Object req) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object receive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Message decodeResp(Object resp) {
		throw new UnsupportedOperationException();
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object encodeSUIS(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Operation> digest() {
		// TODO Auto-generated method stub
		return null;
	}
}
