package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void testAddMember_1() {
		JsonObject obj = new JsonObject();
		obj.add("foo", "bar");
		assertEquals("bar", obj.get("foo").stringValue(""));
	}

	@Test
	public void testAddMember_2() {
		JsonObject obj = new JsonObject();
		obj.add("foo", 1314);
		assertEquals(1314, obj.get("foo").intValue(0));
	}

	@Test
	public void testAddMember_3() {
		JsonObject obj = new JsonObject();
		obj.add("foo", 3.141592);
		assertEquals(3.141592, obj.get("foo").doubleValue(0), 0.00001);
	}

	@Test
	public void testAddMember_4() {
		JsonObject obj = new JsonObject();
		obj.add("foo", true);
		assertEquals(true, obj.get("foo").boolValue(false));
	}

	@Test
	public void testAddMember_5() {
		JsonObject obj = new JsonObject();
		obj.add("foo", false);
		assertEquals(false, obj.get("foo").boolValue(true));
	}
}
