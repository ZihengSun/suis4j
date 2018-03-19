package suis4j.driver;

import java.net.URL;
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
	public PayLoad encodeReq(Message msg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void send(PayLoad req) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PayLoad receive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Message decodeSUIS(PayLoad rawmsg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayLoad encodeSUIS(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Operation> digest() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class Builder extends AbstractDriver.Builder{

		@Override
		public Builder parse(String descfile) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Builder access_endpoint(URL url) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Builder desc_endpoint(URL url) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Builder type(ServiceType type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AbstractDriver build() {
			// TODO Auto-generated method stub
			return null;
		}

		
		
	}
	
}
