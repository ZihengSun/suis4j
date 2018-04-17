package org.suis4j;


import org.jdom2.Element;


public class MessageTreeIterator
{
	private Element element;

	public MessageTreeIterator(Element element)
	{
		this.element = element;
	}

	public MessageTreeIterator set(String key, String value)
	{
		Element child = element.getChild(key);
		if(child == null)
		{
			child = new Element(key);
			element.addContent(child);
		}

		child.setText(value);

		return this;
	}

	public String get(String key)
	{
		Element child = element.getChild(key);
		if(child == null)
		{
			return null;
		}
		else 
		{
			return child.getText();
		}
	}
}