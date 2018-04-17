package org.suis4j;

import org.jdom2.Document;
import org.jdom2.Element;


public class Message
{
	private MessageTreeIterator rootIter;

	public Message()
	{
		rootIter = new MessageTreeIterator(new Element("MessageRootElement"));
	}

	public MessageTreeIterator build()
	{
		return rootIter;
	}

	public MessageTreeIterator query()
	{
		return rootIter;
	}

	public Schema schema()
	{
		return new Schema();
	}
}