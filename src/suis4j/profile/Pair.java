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
	
	
	
}
