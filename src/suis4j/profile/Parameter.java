package suis4j.profile;
/**
*Class Parameter.java
*@author Ziheng Sun
*@time Dec 8, 2017 1:33:59 PM
*/
public class Parameter {
	
	private String name,
				
				namespace,
				/**
				 * Parameter description is optional but strongly recommended. 
				 * It is very important to let users understand what the parameter is used for and how it is used. 
				 */
				description;
	
	/**
	 * This attribute is optional. Default value is 1. Unlimited is -1.  
	 */
	private int occurs = 1;
	
	private DataType type;
	
	private Object value;
	
	protected Parameter(){
		
		
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOccurs() {
		return occurs;
	}

	public void setOccurs(int occurs) {
		this.occurs = occurs;
	}
	
	public static class Builder {

		Parameter p;
		
		public Builder(){
			
			p = new Parameter();
			
		}
		
		public Parameter.Builder name(String n){
			
			p.setName(n);
			
			return this;
			
		}
		
		public Parameter.Builder namespace(String space){
			
			p.setNamespace(space);
			
			return this;
			
		}
		
		public Parameter.Builder description(String desc){		
			
			p.setDescription(desc);
			
			return this;
			
		}
		
		public Parameter.Builder type(DataType t){
			
			p.setType(t);
			
			return this;
			
		}
		
		public Parameter.Builder occurs(int occurs){
			
			p.setOccurs(occurs);
			
			return this;
			
		}
		
		public Parameter build(){
			
			return p;
			
		}
		
	}
	
}
