package suis4j.driver;
/**
*Class AbstractResponseParser.java
*@author Ziheng Sun
*@time Feb 1, 2018 4:05:11 PM
*/
public abstract class AbstractResponseParser {
	
	/**
	 * Parse the direct response from web service into AbstractBody
	 * @param body
	 * @return
	 */
	abstract public AbstractBody parse(Object resp);

}
