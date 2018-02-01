package suis4j.examples;

import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;
import suis4j.profile.Operation;
import suis4j.profile.OperationBuilder;
import suis4j.profile.Parameter;
import suis4j.profile.ParameterBuilder;

/**
*Class TestBuilders.java
*@author Ziheng Sun
*@time Jan 31, 2018 11:11:36 AM
*/
public class TestBuilders {
	
	public static void testOperation(){
		
		Operation o = new OperationBuilder()
				.name("test")
				.description("this is a test")
				.input(null)
				.output(null)
				.build();
		
	}
	
	public static void testParameter(){
		
		Parameter p = new ParameterBuilder()
				.name("param1")
				.description("")
				.type(DataType.FILE)
				.occurs(1)
				.build();

		System.out.println(p.getName());
		
	}
	
	public static void testMessage(){
		
		Message m = new MessageBuilder()
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
