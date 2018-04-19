package suis4j.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*Class Message.java
*@author Ziheng Sun
*@time Jan 31, 2018 12:03:16 PM
*/
public class Message {
	
	List<Parameter> parameter_list;
	
	String error;
	
	/**
	 * This constructor is protected. 
	 * It is recommended to use MessageBuilder to create a new Message object.
	 */
	protected Message(){}
	
	public Map toKVPs(){
		
		Map kvp = new HashMap();
		
		for(int i=0;i<parameter_list.size();i++){
			
//			System.out.println(parameter_list.get(i).getName() + " - " + parameter_list.get(i).getValue());
			
			if(parameter_list.get(i).getValue()!=null){

				kvp.put(parameter_list.get(i).getName(), parameter_list.get(i).getValue());
				
			}
			
		}
		
		return kvp;
		
	}
	
	public void listKVPs(){
		
		System.out.println(">> List output params and values: ");
		
		for(int i=0;i<parameter_list.size();i++){
			
			System.out.println(parameter_list.get(i).getName() + " - " + parameter_list.get(i).getValue());
			
		}
		
	}
	
	public String getValueAsString(String parameter){
		
		Parameter p = this.get(parameter);
		
		String val = "";
		
		if(p.getValue()!=null){
			
			val = String.valueOf(p.getValue());
			
		}
		
		return val;
		
	}
	
	public double getValueAsDouble(String parameter){
		
		return Double.valueOf(getValueAsString(parameter));
		
	}
	
	public int getValueAsInt(String parameter){
		
		return Integer.valueOf(getValueAsString(parameter));
		
	}
	
	public float getValueAsFloat(String parameter){
		
		return Float.valueOf(getValueAsString(parameter));
		
	}
	
	public boolean getValueAsBoolean(String parameter){
		
		return "true".equals(getValueAsString(parameter).toLowerCase());
		
	}
	
	public Object value(String parameter){
		
		Parameter p = this.get(parameter);
		
		return p.getValue();
		
	}
	
	public Message value(String parameter, String value){
		
		Parameter p = this.get(parameter);
		
		p.setValue(value);
		
		return this;
		
	}
	
	public Message value(String parameter, double value){
		
		Parameter p = this.get(parameter);
		
		p.setValue(value);
		
		return this;
		
	}
	
	public Message value(String parameter, int value){
		
		Parameter p = this.get(parameter);
		
		p.setValue(value);
		
		return this;
		
	}
	
	public Message value(String parameter, boolean value){
		
		Parameter p = this.get(parameter);
		
		p.setValue(value);
		
		return this;
		
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Parameter get(String name, String namespace){
		
		Parameter p = null;
		
		for(int i=0;i<parameter_list.size();i++){
			
			if(name.equals(parameter_list.get(i).getName()) 
					&& namespace.equals(parameter_list.get(i).getNamespace())){
				
				p = parameter_list.get(i);
				
				break;
				
			}
			
		}
		
		if(p==null){
			
			throw new RuntimeException("Fail to find parameter with name: " + name);
			
		}
		
		return p;
	}
	
	public Parameter get(String name){
		
		Parameter p = null;
		
		int pidx = -1;
		
		for(int i=0;i<parameter_list.size();i++){
			
			if(name.equals(parameter_list.get(i).getName())){
				
				p = parameter_list.get(i);
				
				pidx = i;
				
				break;
				
			}
			
		}
		
//		if(pidx==-1){
//			
//			throw new RuntimeException("Fail to find parameter with name: " + name);
//			
//		}
		
		return p;
	}

	public List<Parameter> getParameter_list() {
		return parameter_list;
	}

	public void setParameter_list(List<Parameter> parameter_list) {
		this.parameter_list = parameter_list;
	}
	
	public static class Builder {

		Message m;
		
		public Builder(){
			
			m = new Message();
			
		}
		
		public Message.Builder params(List<Parameter> paramlist){
			
			m.setParameter_list(paramlist);
			
			return this;
			
		}
		
		public Message.Builder error(String error){
			
			m.setError(error);
			
			return this;
			
		}
		
		public Message build(){
			
			return m;
			
		}
		
	}
	
}
