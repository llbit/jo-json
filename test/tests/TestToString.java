package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.llbit.json.JsonArray;
import se.llbit.json.JsonMember;
import se.llbit.json.JsonNull;
import se.llbit.json.JsonNumber;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonString;
import se.llbit.json.JsonTrue;

public class TestToString {

	@Test
	public void testNumber() {
		assertEquals("123", new JsonNumber("123").toString());
	}

	@Test
	public void testString() {
		assertEquals("\"eat more carrots\"", new JsonString("eat more carrots").toString());
	}

	@Test
	public void testTrue() {
		assertEquals("true", new JsonTrue().toString());
	}

	@Test
	public void testMember() {
		assertEquals("foo : \"bar\"", new JsonMember("foo", new JsonString("bar")).toString());
	}

	@Test
	public void testNull() {
		assertEquals("null", new JsonNull().toString());
	}

	@Test
	public void testObject() {
		JsonObject obj = new JsonObject();
		obj.addMember(new JsonMember("foo", new JsonString("bar")));
		obj.addMember(new JsonMember("cow", new JsonNumber("4")));
		assertEquals("{ foo : \"bar\", cow : 4 }", obj.toString());
	}

	@Test
	public void testArray() {
		JsonArray array = new JsonArray();
		array.addElement(new JsonString("foo"));
		array.addElement(new JsonString("bar"));
		array.addElement(new JsonTrue());
		array.addElement(new JsonNumber("4"));
		assertEquals("[ \"foo\", \"bar\", true, 4 ]", array.toString());
	}
}
