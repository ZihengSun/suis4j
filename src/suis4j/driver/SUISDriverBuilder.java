package suis4j.driver;

import java.net.URL;

/**
*Class SUISDriverBuilder.java
*@author Ziheng Sun
*@time Feb 15, 2018 3:11:44 PM
*/
public class SUISDriverBuilder extends AbstractDriverBuilder {

	SUISDriver driver = new SUISDriver();
	
	public SUISDriverBuilder parse(String descfile){
		
		
		
		return this;
		
	}
	
	public SUISDriver build(){
		
		
		return driver;
		
	}

	@Override
	public AbstractDriverBuilder access_endpoint(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDriverBuilder desc_endpoint(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDriverBuilder type(ServiceType type) {
		// TODO Auto-generated method stub
		return null;
	}

}
