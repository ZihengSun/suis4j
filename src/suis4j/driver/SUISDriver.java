package suis4j.driver;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;

/**
*Class SUISDriver.java
*Only implement two method: decodeSUIS and encodeSUIS
*@author Ziheng Sun
*@time Jan 31, 2018 12:49:51 PM
*/
public class SUISDriver implements AbstractDriver{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public Message decodeSUIS(Object rawmsg) {
		
		logger.info("decode raw message into SUIS message object..");
		
		
		
		Message m = new MessageBuilder()
				.params(null)
				.build();
		
		return m;
	}
	
	@Override
	public Object encodeSUIS(Message msg) {
	
		ObjectMapper objectMapper = new ObjectMapper();
		
		String msgjson = null;
		
		try {
			
			msgjson = objectMapper.writeValueAsString(msg);
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		}
		
		return msgjson;
	}
	
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
