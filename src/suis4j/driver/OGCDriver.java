package suis4j.driver;

import suis4j.profile.Message;

/**
*Class OGCWPS2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:52 AM
*/
public class OGCDriver extends SUISDriver {

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
}
