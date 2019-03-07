package suis4j;

import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class TestBuilders.java
*@author Ziheng Sun
*@time Jan 31, 2018 11:11:36 AM
*/
public class TestBuilders {
	
	public static void testOperation(){
		
		Operation o = new Operation.Builder()
				.name("test")
				.description("this is a test")
				.input(null)
				.output(null)
				.build();
		
	}
	
	public static void testParameter(){
		
		Parameter p = new Parameter.Builder()
				.name("param1")
				.description("")
				.type(DataType.FILE)
				.minoccurs(1)
				.build();

		System.out.println(p.getName());
		
	}
	
	public static void testMessage(){
		
		Message m = new Message.Builder()
				.params(null)
				.build();
		
		System.out.println(m);
		
	}

	public static void main(String[] args) {
		
		testOperation();
		
		testParameter();
		
		testMessage();

	}

}
