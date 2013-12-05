package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import se.llbit.json.JsonNumber;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonString;

@SuppressWarnings("javadoc")
public class TestStringEscape {
	@Test
	public void testUnknown_1() {
		JsonObject obj = new JsonObject();
		obj.add("hello\n", new JsonString(""));
		assertEquals("hello\\n", obj.getMember(0).getName());
	}
}
