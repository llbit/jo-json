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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.llbit.json.JsonParser.SyntaxError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestParsing {
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

  @Test public void testEmptyObject() throws IOException, SyntaxError {
    String json = " { } ";
    assertTrue(parse(json) instanceof JsonObject);
  }

  @Test public void testOneMemberObject_1() throws IOException, SyntaxError {
    JsonObject object = (JsonObject) parse(" { \"a\": 0 } ");
    assertEquals(1, object.size());
    assertTrue(object.get(0) != null);
    assertTrue(object.get(0).getValue() instanceof JsonNumber);
  }

  @Test public void testNestedObjectArray_1() throws IOException, SyntaxError {
    JsonObject object = (JsonObject) parse(" { \"a\": [ 0, 1, 2 ] } ");
    assertEquals(1, object.size());
    assertTrue(object.get(0) != null);
    testArray(object.get(0).getValue(), JsonNumber.class, JsonNumber.class, JsonNumber.class);
  }

  @Test public void testEmptyArray() throws IOException, SyntaxError {
    assertTrue(parse("[]") instanceof JsonArray);
  }

  @Test public void testOneElementArray_1() throws IOException, SyntaxError {
    testArray(parse("[ false ]"), Json.FALSE.getClass());
  }

  @Test public void testOneElementArray_2() throws IOException, SyntaxError {
    testArray(parse("[ 12 ]"), JsonNumber.class);
  }

  @Test public void testMultiElementArray_1() throws IOException, SyntaxError {
    testArray(parse("[ 12, -3 ]"), JsonNumber.class, JsonNumber.class);
  }

  @Test public void testMultiElementArray_2() throws IOException, SyntaxError {
    testArray(parse("[ true, 1000, \"a\", null, -3 ]"),
        Json.TRUE.getClass(), JsonNumber.class, JsonString.class,
        Json.NULL.getClass(), JsonNumber.class);
  }

  @Test public void testString_1() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ \"hello\" ]");
    assertTrue(array.get(0) instanceof JsonString);
    JsonString string = (JsonString) array.get(0);
    assertEquals("hello", string.value);
  }

  /** Parsing escaped characters. */
  @Test public void testString_2() throws IOException, SyntaxError {
    JsonArray array = parse("[ \"\r\n\", \"\\u0041\\u0062\\u002B\\u002e\", \"1\\/0\" ]").array();
    assertTrue(array.get(0) instanceof JsonString);
    assertEquals("\r\n", array.get(0).asString(""));
    assertEquals("Ab+.", array.get(1).asString(""));
    assertEquals("1/0", array.get(2).asString(""));
  }

  @Test public void testNumber_1() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ 0 ]");
    assertTrue(array.get(0) instanceof JsonNumber);
    JsonNumber number = (JsonNumber) array.get(0);
    assertEquals("0", number.value);
  }

  @Test public void testNumber_2() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ -13 ]");
    assertTrue(array.get(0) instanceof JsonNumber);
    JsonNumber number = (JsonNumber) array.get(0);
    assertEquals("-13", number.value);
  }

  @Test public void testTrue() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ true ]");
    assertSame(Json.TRUE, array.get(0));
  }

  @Test public void testFalse() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ false ]");
    assertSame(Json.FALSE, array.get(0));
  }

  @Test public void testNull() throws IOException, SyntaxError {
    JsonArray array = (JsonArray) parse("[ null ]");
    assertSame(Json.NULL, array.get(0));
  }

  @Test public void testMissingArrayElement_1() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing element in array");
    parse("[ false, ]");
  }

  @Test public void testMissingArrayElement_2() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing element in array");
    parse("[ , false ]");
  }

  @Test public void testMissingArrayElement_3() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: missing element in array");
    parse("[ , ]");
  }

  @Test public void testTrailingGarbage_1() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: garbage at end of input (unexpected 'x')");
    parse("[ ] x");
  }

  @Test public void testTrailingGarbage_2() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: garbage at end of input (unexpected '[')");
    parse("[ ] [ ]");
  }

  @Test public void testIllegalEscape1() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: illegal escape sequence in JSON string: \\?. "
        + "Expected one of \\n, \\r, \\t, etc.");
    parse("[ \"\\?\" ]");
  }

  /** Only lower-case u is used for Unicode escape sequences. */
  @Test public void testIllegalEscape2() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: illegal escape sequence in JSON string: \\U. "
        + "Expected one of \\n, \\r, \\t, etc.");
    parse("[ \"\\U0061\" ]"); // Error: only lower-case u is used for unicode escapes.
  }

  /** Only hexadecimal digits supported in Unicode escape sequences. */
  @Test public void testIllegalEscape3() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage(
        "Syntax Error: in JSON string: non-hexadecimal digit 'U' in Unicode escape sequence.");
    parse("[ \"\\uAFUE\" ]");
  }

  /** Only hexadecimal digits supported in Unicode escape sequences. */
  @Test public void testIllegalEscape4() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage(
        "Syntax Error: in JSON string: non-hexadecimal digit '.' in Unicode escape sequence.");
    parse("[ \"\\u.\" ]");
  }

  /** Only hexadecimal digits supported in Unicode escape sequences. */
  @Test public void testIllegalEscape5() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage(
        "Syntax Error: in JSON string: non-hexadecimal digit '@' in Unicode escape sequence.");
    parse("[ \"\\u@\" ]");
  }

  /** Only hexadecimal digits supported in Unicode escape sequences. */
  @Test public void testIllegalEscape6() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage(
        "Syntax Error: in JSON string: non-hexadecimal digit '_' in Unicode escape sequence.");
    parse("[ \"\\u_\" ]");
  }

  /** Only hexadecimal digits supported in Unicode escape sequences. */
  @Test public void testIllegalEscape7() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage(
        "Syntax Error: in JSON string: non-hexadecimal digit '}' in Unicode escape sequence.");
    parse("[ \"\\u}\" ]");
  }

  /** String terminating in the middle of escape sequence. */
  @Test public void testEofInString() throws IOException, SyntaxError {
    thrown.expect(JsonParser.SyntaxError.class);
    thrown.expectMessage("Syntax Error: end of input in JSON string escape sequence.");
    parse("[ \"abc\\");
  }

  private static void testArray(JsonValue value, Class<?>... elementTypes) {
    assertTrue(value instanceof JsonArray);
    JsonArray array = (JsonArray) value;
    assertEquals(elementTypes.length, array.size());
    for (int i = 0; i < elementTypes.length; ++i) {
      assertEquals(elementTypes[i], array.get(i).getClass());
    }
  }

}
