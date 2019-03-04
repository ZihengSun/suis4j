package suis4j.driver;
/**
*Class AbstractBody.java
*@author Ziheng Sun
*@time Feb 1, 2018 4:34:41 PM
*/
public class PayLoad {
	
	Object content;
	
	protected PayLoad(){}
	
	public Object getContent() {
		return content;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}
	
	public static class Builder {
		
		PayLoad load;
		
		public Builder(){
			
			load = new PayLoad();
			
		}
		
		public Builder content(Object content){
			
			load.setContent(content);
			
			return this;
			
		}
		
		public PayLoad build(){
			
			return load;
			
		}
		
	}
	
}
