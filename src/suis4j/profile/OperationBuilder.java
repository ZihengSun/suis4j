package suis4j.profile;

import java.util.List;

/**
*Class OperationBuilder.java
*@author Ziheng Sun
*@time Jan 31, 2018 11:06:14 AM
*/
public class OperationBuilder {

	Operation o;
	
	public OperationBuilder(){
		
		o = new Operation();
		
	}
	
	public OperationBuilder name(String n){
		
		o.setName(n);
		
		return this;
		
	}
	
	public OperationBuilder description(String desc){
		
		o.setDescription(desc);
		
		return this;
		
	}
	
	public OperationBuilder input(Message msg){
		
		o.setInput(msg);;
		
		return this;
		
	}
	
	public OperationBuilder output(Message msg){
		
		o.setOutput(msg);
		
		return this;
		
	}
	
	public Operation build(){
		
		return o;
		
	}
	
}
