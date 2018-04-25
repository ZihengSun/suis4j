package org.suis4j;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.*;

import java.util.regex.*;


public class Message
{
	// this is the root element of an XML doc that contains our message parameters tree
	private MessageDataTree data;

	public Message()
	{
		data = new MessageDataTree();
	}

	// set value from string
	public Message value(String key, String value)
	{
		data.setValue(key, value);

		return this;
	}

	// get value string
	public String value(String key)
	{
		return data.getValue(key);
	}

	/** Example: values("img", ["jpeg", "png", "tiff"]
		Creates structure
			<img>
	 			<format>jpeg</format>
	 			<format>png</format>
	 			<format>tiff</format>
	 		</img>
	**/
	public Message values(String key, List<String> values)
	{
		data.setValuesList(key, values);
		return this;
	}

	// get a list of values
	public List<String> values(String key)
	{
		return data.getValuesList(key);
	}

	// get a list of keys for nested keys
	public List<String> keys(String key)
	{
		return data.keys(key);
	}

	public List<String>  keys()
	{
		return data.keys("");
	}
}