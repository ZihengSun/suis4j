package suis4j.driver;

import java.net.URL;

/**
*Class AbstractDriverBuilder.java
*@author Ziheng Sun
*@time Feb 1, 2018 6:58:18 PM
*/
public abstract class AbstractDriverBuilder {
	
	public abstract AbstractDriverBuilder parse(String descfile);
	
	public abstract AbstractDriverBuilder access_endpoint(URL url);
	
	public abstract AbstractDriverBuilder desc_endpoint(URL url);
	
	public abstract AbstractDriverBuilder type(ServiceType type);
	
	abstract public AbstractDriver build();

}
