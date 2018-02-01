package suis4j.profile;

import java.util.List;

/**
*Class MessageBuilder.java
*@author Ziheng Sun
*@time Jan 31, 2018 12:27:34 PM
*/
public class MessageBuilder {

	Message m;
	
	public MessageBuilder(){
		
		m = new Message();
		
	}
	
	public MessageBuilder params(List<Parameter> paramlist){
		
		m.setParameter_list(paramlist);
		
		return this;
		
	}
	
	public Message build(){
		
		return m;
		
	}
	
}
