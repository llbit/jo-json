package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.llbit.json.JsonNumber;

@SuppressWarnings("javadoc")
public class TestJsonNumber {
	@Test
	public void testDoubleFormat_1() {
		JsonNumber num = new JsonNumber(3.141592);
		assertEquals(3.141592, num.doubleValue(0), 0.00001);
	}
}
