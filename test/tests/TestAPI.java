package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import se.llbit.json.JsonNumber;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonString;

@SuppressWarnings("javadoc")
public class TestAPI {
	@Test
	public void testUnknown_1() {
		JsonObject obj = new JsonObject();
		assertTrue(obj.get("abc").isUnknown());
	}

	@Test
	public void testStringValue_1() {
		JsonString str = new JsonString("hello");
		assertEquals("hello", str.stringValue(""));
	}

	@Test
	public void testStringValue_2() {
		JsonNumber num = new JsonNumber("123");
		assertEquals("tmnt", num.stringValue("tmnt"));
	}
}
