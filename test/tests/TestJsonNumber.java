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
	@Test
	public void testDoubleFormat_2() {
		JsonNumber num = new JsonNumber(Double.NEGATIVE_INFINITY);
		assertEquals(Double.NEGATIVE_INFINITY, num.doubleValue(0), 0);
	}
	@Test
	public void testDoubleFormat_3() {
		JsonNumber num = new JsonNumber(Double.POSITIVE_INFINITY);
		assertEquals(Double.POSITIVE_INFINITY, num.doubleValue(0), 0);
	}
	@Test
	public void testDoubleFormat_4() {
		JsonNumber num = new JsonNumber(Double.NaN);
		assertEquals(Double.NaN, num.doubleValue(0), 0);
	}
}
