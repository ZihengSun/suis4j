package org.suis4j;

public class Message
{
	public Message()
	{
		
	}

	public MessageTreeIterator build()
	{
		return new MessageTreeIterator();
	}

	public MessageTreeIterator query()
	{
		return new MessageTreeIterator();
	}

	public Schema schema()
	{
		return new Schema();
	}

}