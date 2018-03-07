package suis4j.profile;

import java.util.List;

/**
*Class Message.java
*@author Ziheng Sun
*@time Jan 31, 2018 12:03:16 PM
*/
public class Message {
	
	List<Parameter> parameter_list;
	
	Parameter p = null;
	
	int pidx = -1;
	
	/**
	 * This constructor is protected. 
	 * It is recommended to use MessageBuilder to create a new Message object.
	 */
	protected Message(){}
	
	public Message value(String parameter, String value){
		
		this.get(parameter, p, pidx);
		
		p.setValue(value);
		
		parameter_list.set(pidx, p);
		
		return this;
		
	}
	
	public Message value(String parameter, double value){
		
		this.get(parameter, p, pidx);
		
		p.setValue(value);
		
		parameter_list.set(pidx, p);
		
		return this;
		
	}
	
	public Message value(String parameter, int value){
		
		this.get(parameter, p, pidx);
		
		p.setValue(value);
		
		parameter_list.set(pidx, p);
		
		return this;
		
	}
	
	public Message value(String parameter, boolean value){
		
		this.get(parameter, p, pidx);
		
		p.setValue(value);
		
		parameter_list.set(pidx, p);
		
		return this;
		
	}
	
	public void get(String name, Parameter p, int idx){
		
		p = null;
		
		pidx = -1;
		
		for(int i=0;i<parameter_list.size();i++){
			
			if(name.equals(parameter_list.get(i).getName())){
				
				p = parameter_list.get(i);
				
				idx = i;
				
				break;
				
			}
			
		}
		
		if(pidx==-1){
			
			throw new RuntimeException("Fail to find parameter with name: " + name);
			
		}
		
	}

	public List<Parameter> getParameter_list() {
		return parameter_list;
	}

	public void setParameter_list(List<Parameter> parameter_list) {
		this.parameter_list = parameter_list;
	}
	
}
