/* Copyright (c) 2013-2017, Jesper Öqvist
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
