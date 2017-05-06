package se.llbit.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestJsonNumber {
  @Test public void testInt1() {
    JsonNumber num = new JsonNumber(1);
    assertEquals(1, num.intValue(0));
    assertEquals(1, num.asInt(0));
    assertEquals(1, num.longValue(0));
    assertEquals(1, num.asLong(0));

    num = new JsonNumber(-1);
    assertEquals(-1, num.intValue(0));
    assertEquals(-1, num.asInt(0));
    assertEquals(-1, num.longValue(0));
    assertEquals(-1, num.asLong(0));
  }

  @Test public void testFloat1() {
    JsonNumber num = new JsonNumber(3.141592f);
    assertEquals(3.141592f, num.floatValue(0), 0.00001f);
    assertEquals(3.141592f, num.asFloat(0), 0.00001f);
  }

  /**
   * Negative infinity is preserved in a JSON number.
   */
  @Test public void testFloatNegInf() {
    JsonNumber num = new JsonNumber(Float.NEGATIVE_INFINITY);
    assertEquals(Float.NEGATIVE_INFINITY, num.floatValue(0), 0);
    assertEquals(Float.NEGATIVE_INFINITY, num.asFloat(0), 0);
  }

  /**
   * Positive infinity is preserved in a JSON number.
   */
  @Test public void testFloatPosInf() {
    JsonNumber num = new JsonNumber(Float.POSITIVE_INFINITY);
    assertEquals(Float.POSITIVE_INFINITY, num.floatValue(0), 0);
    assertEquals(Float.POSITIVE_INFINITY, num.asFloat(0), 0);
  }

  /**
   * NaN is preserved in a JSON number.
   */
  @Test public void testFloatNaN() {
    JsonNumber num = new JsonNumber(Float.NaN);
    assertEquals(Float.NaN, num.floatValue(0), 0);
    assertEquals(Float.NaN, num.asFloat(0), 0);
  }

  @Test public void testDouble1() {
    JsonNumber num = new JsonNumber(3.141592);
    assertEquals(3.141592, num.doubleValue(0), 0.00001);
    assertEquals(3.141592, num.asDouble(0), 0.00001);
  }

  /**
   * Negative infinity is preserved in a JSON number.
   */
  @Test public void testDoubleNegInf() {
    JsonNumber num = new JsonNumber(Double.NEGATIVE_INFINITY);
    assertEquals(Double.NEGATIVE_INFINITY, num.doubleValue(0), 0);
    assertEquals(Double.NEGATIVE_INFINITY, num.asDouble(0), 0);
  }

  /**
   * Positive infinity is preserved in a JSON number.
   */
  @Test public void testDoublePosInf() {
    JsonNumber num = new JsonNumber(Double.POSITIVE_INFINITY);
    assertEquals(Double.POSITIVE_INFINITY, num.doubleValue(0), 0);
    assertEquals(Double.POSITIVE_INFINITY, num.asDouble(0), 0);
  }

  /**
   * NaN is preserved in a JSON number.
   */
  @Test public void testDoubleNaN() {
    JsonNumber num = new JsonNumber(Double.NaN);
    assertEquals(Double.NaN, num.doubleValue(0), 0);
    assertEquals(Double.NaN, num.asDouble(0), 0);
  }
}
