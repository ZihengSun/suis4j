package suis4j.driver;

import java.util.ArrayList;
import java.util.List;

/**
*Class DriverManager.java
*@author Ziheng Sun
*@time Feb 15, 2018 2:37:55 PM
*/
public class DriverManager {

	private static List<AbstractDriver> drivers = new ArrayList();
	
	private DriverManager(){}
	
	public static AbstractDriver create(ServiceType st){
		
		AbstractDriver ad = null;
		
		switch(st){
		
			case SOAP: ad = new SOAPDriver(); break;
			case REST: ad = new RESTDriver(); break;
			case OGC: ad = new OGCDriver();break;
		
		}
		
		return ad;
		
	}
	
	/**
	 * Get AbstractDriver
	 * @param oname
	 * @return
	 */
	public static AbstractDriver get(String driverid){
		
		AbstractDriver ad = null;
		
		for(AbstractDriver d: drivers){
			
			if(d.getId().equals(driverid)){
				
				ad = d;
				
				break;
				
			}
		}
		
		return ad;
		
	}
	
	/**
	 * Add driver into the static list
	 * @param driver
	 */
	public static void add(AbstractDriver driver){
		
		drivers.add(driver);
		
	}
	
	/**
	 * Remove the driver from list
	 * @param driver
	 */
	public static void remove(AbstractDriver driver){
		
		drivers.remove(driver);
		
	}
	
}
