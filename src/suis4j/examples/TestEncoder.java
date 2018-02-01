package suis4j.examples;

import java.util.ArrayList;
import java.util.List;

import suis4j.driver.AbstractDriver;
import suis4j.driver.SUISDriver;
import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;
import suis4j.profile.Parameter;

/**
*Class TestEncoder.java
*@author Ziheng Sun
*@time Jan 31, 2018 3:43:55 PM
*/
public class TestEncoder {

	public static void main(String[] args) {
		
		AbstractDriver driver = new SUISDriver();
		
		List<Parameter> paramlist = new ArrayList();
		
		Message msg = new MessageBuilder()
				.params(paramlist)
				.build();
		
		String suismsg = (String)driver.encodeSUIS(msg);
		
		System.out.println(suismsg);
		
	}

}
