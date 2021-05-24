/* Copyright (c) 2017, Jesper Öqvist
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * API tests for the JSON library.
 */
public class TestJson {
  @Rule public ExpectedException thrown = ExpectedException.none();

  private static JsonValue parse(String json) throws IOException, JsonParser.SyntaxError {
    InputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    JsonParser parser = new JsonParser(input);
    try {
      return parser.parse();
    } finally {
      parser.close();
    }
  }

  /** Possible to parse empty objects. */
  @Test public void testEmptyObject() throws IOException, JsonParser.SyntaxError {
    assertEquals(0, parse("{}").object().size());
  }

  /** Simple JSON object. */
  @Test public void testObject1() throws IOException, JsonParser.SyntaxError {
    JsonObject object = parse("{\"a\":\"a\", \"b\": -1}").object();
    assertEquals(2, object.size());
    assertEquals("a", object.get("a").stringValue(""));
    assertEquals(-1, object.get("b").intValue(8));
  }

  /** Nested objects. */
  @Test public void testObject2() throws IOException, JsonParser.SyntaxError {
    JsonObject object = parse("{\"a\": {\"x\": true, \"y\": {\"z\":16.0}}, \"b\": []}").object();
    assertEquals(2, object.size());
    assertEquals(2, object.get("a").object().size());
    assertEquals(true, object.get("a").object().get("x").boolValue(false));
    assertEquals(1, object.get("a").object().get("y").object().size());
    assertEquals(16, object.get("a").object().get("y").object().get("z").doubleValue(0), 0.01);
    assertTrue(object.get("b").isArray());
  }

  /** Only the first duplicate member name can be indexed in an object. */
  @Test public void testObject3() throws IOException, JsonParser.SyntaxError {
    assertEquals("first",
        parse("{\"x\" : \"first\", \"x\" : \"second\"}").object().get("x").stringValue(""));
    assertEquals(-10,
        parse("{\"bort\" : 3, \"x\" : -10, \"x\" : null}").object().get("x").intValue(0));
  }

  /** Accessing object members with strange names. */
  @Test public void testObject4() {
    JsonObject object = new JsonObject();
    object.add("\"", 1);
    object.add("\\", 2);
    object.add("\n", 3);
    object.add("\r", 4);
    object.add("\t", 5);
    object.add("\b", 6);
    object.add("\f", 7);
    object.add(" ", 8);
    assertEquals(1, object.get("\"").intValue(0));
    assertEquals(2, object.get("\\").intValue(0));
    assertEquals(3, object.get("\n").intValue(0));
    assertEquals(4, object.get("\r").intValue(0));
    assertEquals(5, object.get("\t").intValue(0));
    assertEquals(6, object.get("\b").intValue(0));
    assertEquals(7, object.get("\f").intValue(0));
    assertEquals(8, object.get(" ").intValue(0));
  }

  /** Possible to parse empty arrays. */
  @Test public void testEmptyArray() throws IOException, JsonParser.SyntaxError {
    assertEquals(0, parse("[]").array().size());
  }

  /** JsonArray.getNumElement() gives the number of array elements. */
  @Test public void testArray1() throws IOException, JsonParser.SyntaxError {
    assertEquals(2, parse("[1, 2]").array().size());
    assertEquals(3, parse("[1, 2, 3]").array().size());
    assertEquals(0, parse("[[]]").array().get(0).array().size());
  }

  /** JsonArray.getElement() is equivalent to JsonArray.get(). */
  @Test public void testArray2() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[{}, [1,2,3]]").array();
    assertSame(array.get(0), array.get(0));
  }

  /** Test the isObject() and isArray methods methods. */
  @Test public void testIsObjectArray() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[{}, [1,2,3], 3, \"foo\", true, false]").array();
    assertTrue(array.get(0).isObject());
    assertTrue(array.get(1).isArray());
    assertFalse(array.get(2).isArray());
    assertFalse(array.get(3).isArray());
  }

  /** Test numbers. */
  @Test public void testBool() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[true, false]").array();
    assertEquals(true, array.get(0).boolValue(false));
    assertEquals(true, array.get(0).asBoolean(false));
    assertEquals(false, array.get(1).boolValue(true));
    assertEquals(false, array.get(1).asBoolean(true));
  }

  /** Test null value. */
  @Test public void testNull() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[null]").array();
    assertSame(Json.NULL, array.get(0));
  }

  /** Test numbers. */
  @Test public void testNumber1() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[0, 1, 2, 3]").array();
    assertEquals(0, array.get(0).intValue(-1));
    assertEquals(1, array.get(1).longValue(-1));
    assertEquals(2, array.get(2).floatValue(-1), 0.001);
    assertEquals(3, array.get(3).doubleValue(-1), 0.001);
  }

  /** Test non-number conversions. */
  @Test public void testNumber4() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[[], {}, true, false]").array();
    assertEquals(-1, array.get(0).intValue(-1));
    assertEquals(-2, array.get(1).longValue(-2));
    assertEquals(-3, array.get(2).floatValue(-3), 0.001);
    assertEquals(-4, array.get(3).doubleValue(-4), 0.001);
  }

  /** Test string parsing. */
  @Test public void testString1() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[\"foo\", \"bart\"]").array();
    assertEquals("foo", array.get(0).stringValue(""));
    assertEquals("bart", array.get(1).stringValue(""));
  }

  /** Test non-string conversions. */
  @Test public void testString2() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[null, [], {}]").array();
    assertEquals("foo", array.get(0).stringValue("foo"));
    assertEquals("bart", array.get(1).stringValue("bart"));
    assertEquals("bort", array.get(2).stringValue("bort"));
  }

  /** Unknown value is replaced by the given defaults. */
  @Test public void testUnknown1() throws IOException, JsonParser.SyntaxError {
    assertEquals("bort", Json.UNKNOWN.stringValue("bort"));
    assertEquals(true, Json.UNKNOWN.boolValue(true));
  }

  /** Unknown is returned when accessing non-existing member. */
  @Test public void testUnknown2() throws IOException, JsonParser.SyntaxError {
    JsonObject object = parse("{\"xyz\" : 123}").object();
    assertTrue(object.get("bort").isUnknown());
    assertTrue(object.get("123").isUnknown());
    assertFalse(object.get("xyz").isUnknown());
  }

  /** Inconvertible types result in the given defaults. */
  @Test public void testWrongType() throws IOException, JsonParser.SyntaxError {
    JsonArray array = parse("[1, false]").array();
    assertEquals("bort", array.get(0).stringValue("bort"));
    assertEquals(100, array.get(1).intValue(100));
  }

  /** Values can be surrounded by whitespace. */
  @Test public void testWhitespace() throws IOException, JsonParser.SyntaxError {
    assertEquals(2, parse("[1,      2]").array().size());
    assertEquals(2, parse("\t  [1, \n2, 3]\r").array().get(1).intValue(0));
    assertEquals(3, parse("\t  [1, \n2, 3]\r").array().get(2).intValue(0));
    assertEquals(3, parse("{\"x\"   \n\r\t   :3}").object().get("x").intValue(0));
  }

  /** Object keys can be missing quotes */
  @Test public void testMissingQuotes() throws JsonParser.SyntaxError, IOException {
    JsonObject object = parse("{abc:\"foo\"}").object();
    assertEquals("foo", object.get("abc").stringValue(""));
  }

  /** Trailing comma in array. */
  @Test public void testSyntaxError1() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing element in array");
    parse("[1,2,]");
  }

  /** Unmatched left brace. */
  @Test public void testSyntaxError2() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected end of input (expected '}')");
    parse("{ ");
  }

  /** Unmatched right brace. */
  @Test public void testSyntaxError3() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: garbage at end of input (unexpected '}')");
    parse("{ \"x\" : 123 } }");
  }

  /** Unclosed quote. */
  @Test public void testSyntaxError4() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: end of input while parsing JSON string (expected '\"')");
    parse("[\",2]");
  }

  /** Stuttered comma in array. */
  @Test public void testSyntaxError5() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing element in array");
    parse("[1,,2]");
  }

  /** Unmatched left bracket. */
  @Test public void testSyntaxError6() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected end of input (expected ']')");
    parse("[");
  }

  /** Unmatched right bracket. */
  @Test public void testSyntaxError7() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: expected JSON object or array");
    parse("]");
  }

  /** Missing colon after member name. */
  @Test public void testSyntaxError8() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected character (was '2', expected ':')");
    parse("{\"01\" 23}");
  }

  /** Misplaced colon in object. */
  @Test public void testSyntaxError9() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected character (was ':', expected '}')");
    parse("{\"01\" : 23 :}");
  }

  /** Misplaced stuttered colon in object. */
  @Test public void testSyntaxError10() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing value for object member");
    parse("{\"01\" :: 23}");
  }

  /** Misspelled keyword. */
  @Test public void testSyntaxError11() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: encountered invalid JSON literal");
    parse("[tru]");
  }

  /** Misspelled keyword (incorrect capitalization). */
  @Test public void testSyntaxError12() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected character (was 'F', expected ']')");
    parse("[False]");
  }

  /** Misspelled keyword. */
  @Test public void testSyntaxError13() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: encountered invalid JSON literal");
    parse("[nul]");
  }

  /** Missing comma in object. */
  @Test public void testSyntaxError14() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: unexpected character (was '\"', expected '}')");
    parse("{\"01\" : 23 \"45\" : 67}");
  }

  /** Empty string. */
  @Test public void testSyntaxError15() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: expected JSON object or array");
    parse("");
  }

  /** Just whitespace. */
  @Test public void testSyntaxError16() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: expected JSON object or array");
    parse(" \t\n\r   \t\n");
  }

  /** Non-string member names are not supported. */
  @Test public void testSyntaxError17() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing member in object.");
    parse("{ 12: true }");
  }

  /** End of input after number. */
  @Test public void testSyntaxError18() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: end of input while parsing JSON number.");
    parse("{ \"abc\": 123");
  }

  /** Missing member in object. */
  @Test public void testSyntaxError19() throws IOException, JsonParser.SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing member in object.");
    parse("{ ,");
  }

  @Test public void testToMap1() {
    JsonArray array = new JsonArray();
    array.add("!");
    array.add(711);
    JsonObject object = new JsonObject();
    object.add(" ab cd", 123);
    object.add("@", "''''");
    object.add("\"\"", "\n\r");
    object.add(".", array);
    object.add("@", "notfirst");

    Map<String, JsonValue> map = object.toMap();
    assertEquals(123, map.get(" ab cd").intValue(0));
    assertEquals("''''", map.get("@").stringValue(""));
    assertEquals("\n\r", map.get("\"\"").stringValue(""));
    assertSame(array, map.get("."));
  }

  /** Test that the map can be modified. */
  @Test public void testToMap2() {
    JsonObject object = new JsonObject();
    object.add("Hunrath", 1);
    object.add("Kaptar", 2);

    Map<String, JsonValue> map = object.toMap();
    assertEquals(2, map.size());

    // Modify the map.
    map.put("Soria", Json.of(4));
    assertEquals(3, map.size());
  }

  /** Test that an empty map can be modified. */
  @Test public void testToMap3() {
    Map<String, JsonValue> map = new JsonObject().toMap();
    assertEquals(0, map.size());

    // Modify the map.
    map.put("Maray", Json.of(3));
    assertEquals(1, map.size());
  }
}
