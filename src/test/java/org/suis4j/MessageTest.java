package org.suis4j;

import java.util.*;

import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class MessageTest
{

	@Test
	public void testMessage()
	{
		Message m = new Message();

		MessageTreeIterator builder = m.build();
		MessageTreeIterator query 	= m.query();
		Schema schema 				= m.schema();
	}
}