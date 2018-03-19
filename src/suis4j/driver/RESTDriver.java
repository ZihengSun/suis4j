package suis4j.driver;

import java.net.URL;
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
	public Message decodeSUIS(PayLoad rawmsg) {
		
		return null;
	}

	@Override
	public PayLoad encodeReq(Message msg) {
		
		return null;
	}

	@Override
	public void send(PayLoad req) {
		
		
	}

	@Override
	public PayLoad receive() {
		
		return null;
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		
		return null;
	}

	@Override
	public PayLoad encodeSUIS(Message msg) {

		return null;
	}

	@Override
	public List<Operation> digest() {
		
		return null;
	}
	
	public static class Builder extends AbstractDriver.Builder {
		
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
