package org.suis4j;

import java.util.*;

import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class MessageBuildAndQueryTest
{
	Message m;

	@Before
	public void buildMessageFixture()
	{
		m = new Message();
	}
	// 	// build a message tree
	// 	m.build()
	// 	.set("imageurl", "http://example.org/large_image.jpeg")
	// 	.element("boundingbox") // begin 'boundingbox'
	// 		.attribute("coordinate_system", "WGS84")
	// 		.set("top", "10")
	// 		.set("left", "10")
	// 		.set("bottom", "100")
	// 		.element("right")
	// 			.text("100")
	// 		.end() // complete <right>
	// 	.end(); // complete <boundingbox>

	// 	// call build() second time to test adding more elements to the existing message tree
	// 	m.build()
	// 	.set("response_format", "jpeg")
	// 	.set("jpeg_quality", "80");

	// 	return m;
	// }

	@Test
	public void testSetGetField()
	{
		m.build()
		.set("field1", "val1")
		.set("field2", "val2");

		// call build second time to test adding additional fields
		m.build().set("field3", "val3");

		// overwrite
		m.build().set("field2", "val2-new");

		// assert
		assertEquals(m.query().get("field1"), "val1");
		assertEquals(m.query().get("field2"), "val2-new");
		assertEquals(m.query().get("field3"), "val3");
		assertEquals(m.query().get("field4"), null);
	}
}