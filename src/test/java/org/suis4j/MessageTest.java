package org.suis4j;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class MessageTest
{
	private Message m;

	@Before
	public void createMessage()
	{
		m = new Message();
	}

	@Test
	public void testMessageSimpleValue()
	{
		m.value("param1", "val1");
		m.value("param2", "val2");

		assertEquals("val1", m.value("param1"));
		assertEquals("val2", m.value("param2"));
	}

	@Test
	public void testMessageTreeValue()
	{
		m.value("parent child1", "a");
		m.value("parent child2", "b");

		assertArrayEquals(new String[]{"parent"}, m.keys().toArray());
		assertArrayEquals(new String[]{"child1", "child2"}, m.keys("parent").toArray());

		assertEquals("a", m.value("parent child1"));
		assertEquals("b", m.value("parent child2"));
	}

	@Test
	public void testMessageTreeArrayValues()
	{
		// set using array notation
		m.value("parentOne listOne[0]", "a");
		m.value("parentOne listOne[1]", "b");
		m.value("parentTwo listOne[2]", "x");

		// read list of values
		assertArrayEquals(new String[]{"a", "b"}, m.values("parentOne listOne").toArray());
		assertArrayEquals(new String[]{"", "", "x"}, m.values("parentTwo listOne").toArray());


		// set using Message::values method
		m.values("listTwo", Arrays.asList("c", "d"));

		// read values as keys
		assertEquals("c", m.value("listTwo[0]"));
		assertEquals("d", m.value("listTwo[1]"));
		assertEquals("", m.value("listTwo[2]"));
	}
}