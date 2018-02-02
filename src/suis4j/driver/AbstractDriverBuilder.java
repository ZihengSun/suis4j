package suis4j.driver;

import java.net.URL;

/**
*Class AbstractDriverBuilder.java
*@author Ziheng Sun
*@time Feb 1, 2018 6:58:18 PM
*/
public abstract class AbstractDriverBuilder {
	
	AbstractDriver ad;
	
	public AbstractDriverBuilder access_endpoint(URL url){
	
		ad.setAccess_endpoint(url);
		
		return this;
		
	}
	
	public AbstractDriverBuilder desc_endpoint(URL url){
		
		ad.setDesc_endpoint(url);
		
		return this;
		
	}
	
	public AbstractDriverBuilder type(ServiceType type){
		
		ad.setServicetype(type);
		
		return this;
		
	}
	
	abstract public AbstractDriver build();

}
