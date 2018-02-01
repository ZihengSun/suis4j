package suis4j.profile;

import suis4j.driver.AbstractDriver;

/**
*Class PairBuilder.java
*@author Ziheng Sun
*@time Feb 1, 2018 4:05:49 PM
*/
public class PairBuilder {
	
	Pair p;
	
	public PairBuilder(){
		
		p = new Pair();
		
	}
	
	public PairBuilder operation(Operation o){
		
		p.setO(o);
		
		return this;
		
	}
	
	public PairBuilder driver(AbstractDriver d){
		
		p.setD(d);
		
		return this;
		
	}
	
	public Pair build(){
		
		return p;
		
	}
	
}
