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

//	public List<SUISDriver> getDrivers() {
//		return drivers;
//	}
//
//	public void setDrivers(List<SUISDriver> drivers) {
//		this.drivers = drivers;
//	} 
	
	private DriverManager(){
		
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
	 * Add AbstractDriver
	 * @param driver
	 */
	public static void add(AbstractDriver driver){
		
		drivers.add(driver);
		
	}
	
	
	
}
