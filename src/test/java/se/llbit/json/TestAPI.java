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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAPI {
  @Test public void testUnknown_1() {
    JsonObject obj = new JsonObject();
    assertTrue(obj.get("abc").isUnknown());
  }

  @Test public void testStringValue_1() {
    JsonString str = new JsonString("hello");
    assertEquals("hello", str.stringValue(""));
  }

  @Test public void testStringValue_2() {
    JsonNumber num = new JsonNumber("123");
    assertEquals("tmnt", num.stringValue("tmnt"));
  }

  @Test public void testAddMember_1() {
    JsonObject obj = new JsonObject();
    obj.add("foo", "bar");
    assertEquals("bar", obj.get("foo").stringValue(""));
  }

  @Test public void testAddMember_2() {
    JsonObject obj = new JsonObject();
    obj.add("foo", 1314);
    assertEquals(1314, obj.get("foo").intValue(0));
  }

  @Test public void testAddMember_3() {
    JsonObject obj = new JsonObject();
    obj.add("foo", 3.141592);
    assertEquals(3.141592, obj.get("foo").doubleValue(0), 0.00001);
  }

  @Test public void testAddMember_4() {
    JsonObject obj = new JsonObject();
    obj.add("foo", true);
    assertEquals(true, obj.get("foo").boolValue(false));
  }

  @Test public void testAddMember_5() {
    JsonObject obj = new JsonObject();
    obj.add("foo", false);
    assertEquals(false, obj.get("foo").boolValue(true));
  }

  @Test(expected = NullPointerException.class)
  public void testOfNull() {
    Json.of(null);
  }

  @Test public void testAs() {
    assertEquals(123, Json.of(true).asInt(123));
    assertEquals(123, Json.of(true).asLong(123));
    assertEquals(123, Json.of(true).asFloat(123), 0.0001);
    assertEquals(123, Json.of(true).asDouble(123), 0.0001);
    assertEquals("123", Json.of(true).asString("123"));
    assertEquals(true, Json.of("bort").asBoolean(true));
    assertEquals(false, Json.of(123).asBoolean(false));
  }

  @Test public void testStringEscape() {
    assertEquals("abc\\ndef", JsonString.escape("abc\ndef"));
  }

  @Test public void printUnknown() throws Exception {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(stream);
    try (PrettyPrinter printer = new PrettyPrinter("", printStream)) {
      printer.print(Json.UNKNOWN);
    }
    assertEquals("\"<unknown>\"", stream.toString());
  }
}
