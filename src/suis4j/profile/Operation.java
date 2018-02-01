package suis4j.profile;

import java.util.List;

/**
*Class Operation.java
*SUIS Operation
*@author Ziheng Sun
*@time Dec 8, 2017 1:35:08 PM
*/
public class Operation {
	
	Message input, output;
	
	String name, description;
	
	protected Operation(){
		
	}
	
	public Message getInput() {
		return input;
	}

	public void setInput(Message input) {
		this.input = input;
	}

	public Message getOutput() {
		return output;
	}

	public void setOutput(Message output) {
		this.output = output;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
