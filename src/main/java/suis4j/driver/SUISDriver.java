package suis4j.driver;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
	public PayLoad encodeSUIS(Message msg) {
		
		String msgjson = null;
		
		try {
			
			msgjson = objectMapper.writeValueAsString(msg);
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		}
		
		return new PayLoad.Builder()
				.content(msgjson)
				.build();
	}
	
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
	public void initParams(Operation o) {
		
	}

	@Override
	public List<Operation> digest() {
		
		return null;
	}

	
	public class Builder extends AbstractDriver.Builder {
		
		SUISDriver driver = new SUISDriver();
		
		public Builder parse(String descfile){
			
			return this;
		}
		
		@Override
		public Builder access_endpoint(URL url) {
			
			return null;
		}

		@Override
		public Builder desc_endpoint(URL url) {
			
			return null;
		}
		
		public AbstractDriver build(){
			
			return driver;
		}
		
	}



	
}
