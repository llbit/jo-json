package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import se.llbit.json.JsonString;

public class TestStringEscape {
	@Test
	public void testUnknown_1() {
		assertEquals("hello\\n", new JsonString("hello\n").escaped());
	}
}
