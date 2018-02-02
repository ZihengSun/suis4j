package suis4j.driver;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import suis4j.profile.Message;
import suis4j.profile.Operation;

/**
*Class SUISDriver.java
*Only implement two method: decodeSUIS and encodeSUIS
*@author Ziheng Sun
*@time Jan 31, 2018 12:49:51 PM
*/
public class SUISDriver extends AbstractDriver{

	Logger logger = Logger.getLogger(this.getClass());
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Message decodeSUIS(Object rawmsg) {
		
		logger.info("decode raw message into SUIS message object..");
		
		Message m = null;
		
		try {
			
			m = objectMapper.readValue((String)rawmsg, Message.class);
			
			logger.info("parameter size :" + m.getParameter_list().size());
			
		} catch (JsonParseException e) {
			
			e.printStackTrace();
			
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		return m;
	}
	
	@Override
	public Object encodeSUIS(Message msg) {
		
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

	@Override
	public Operation disgest() {
		
		return null;
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

	
}
