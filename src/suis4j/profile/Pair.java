package suis4j.profile;

import suis4j.driver.AbstractDriver;

/**
*Class Pair.java
*@author Ziheng Sun
*@time Feb 1, 2018 3:54:29 PM
*/
public class Pair {

	Operation o;
	
	AbstractDriver d;
	
	protected Pair(){
		
	}

	public Operation getO() {
		return o;
	}

	public void setO(Operation o) {
		this.o = o;
	}

	public AbstractDriver getD() {
		return d;
	}

	public void setD(AbstractDriver d) {
		this.d = d;
	} 
	
	public class Builder {
		
		Pair p;
		
		public Builder(){
			
			p = new Pair();
			
		}
		
		public Builder operation(Operation o){
			
			p.setO(o);
			
			return this;
			
		}
		
		public Builder driver(AbstractDriver d){
			
			p.setD(d);
			
			return this;
			
		}
		
		public Pair build(){
			
			return p;
			
		}
		
	}
	
}
