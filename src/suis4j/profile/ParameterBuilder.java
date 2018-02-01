package suis4j.profile;
/**
*Class ParameterBuilder.java
*@author Ziheng Sun
*@time Jan 31, 2018 11:14:42 AM
*/
public class ParameterBuilder {

	Parameter p;
	
	public ParameterBuilder(){
		
		p = new Parameter();
		
	}
	
	public ParameterBuilder name(String n){
		
		p.setName(n);
		
		return this;
		
	}
	
	public ParameterBuilder description(String desc){		
		
		p.setDescription(desc);
		
		return this;
		
	}
	
	public ParameterBuilder type(DataType t){
		
		p.setType(t);
		
		return this;
		
	}
	
	public ParameterBuilder occurs(int occurs){
		
		p.setOccurs(occurs);
		
		return this;
		
	}
	
	public Parameter build(){
		
		return p;
		
	}
	
}
