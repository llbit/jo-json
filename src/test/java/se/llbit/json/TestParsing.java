package se.llbit.json;

import org.junit.Test;
import se.llbit.json.JsonParser.SyntaxError;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Simple JSON parsing tests.
 *
 * @author Jesper Öqvist <jesper.oqvist@cs.lth.se>
 */
public class TestParsing {

  @Test public void testEmptyObject() throws IOException, SyntaxError {
    String json = " { } ";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    assertTrue(parser.parse() instanceof JsonObject);
  }

  @Test public void testOneMemberObject_1() throws IOException, SyntaxError {
    String json = " { \"a\": 0 } ";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonObject object = (JsonObject) parser.parse();
    assertEquals(1, object.getNumMember());
    assertTrue(object.getMember(0) instanceof JsonMember);
    assertTrue(object.getMember(0).getValue() instanceof JsonNumber);
  }

  @Test public void testNestedObjectArray_1() throws IOException, SyntaxError {
    String json = " { \"a\": [ 0, 1, 2 ] } ";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonObject object = (JsonObject) parser.parse();
    assertEquals(1, object.getNumMember());
    assertTrue(object.getMember(0) instanceof JsonMember);
    testArray(object.getMember(0).getValue(), JsonNumber.class, JsonNumber.class, JsonNumber.class);
  }

  @Test public void testEmptyArray() throws IOException, SyntaxError {
    String json = "[]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    assertTrue(parser.parse() instanceof JsonArray);
  }

  @Test public void testOneElementArray_1() throws IOException, SyntaxError {
    String json = "[ false ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    testArray(parser.parse(), Json.FALSE.getClass());
  }

  @Test public void testOneElementArray_2() throws IOException, SyntaxError {
    String json = "[ 12 ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    testArray(parser.parse(), JsonNumber.class);
  }

  @Test public void testMultiElementArray_1() throws IOException, SyntaxError {
    String json = "[ 12, -3 ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    testArray(parser.parse(), JsonNumber.class, JsonNumber.class);
  }

  @Test public void testMultiElementArray_2() throws IOException, SyntaxError {
    String json = "[ true, 1000, \"a\", null, -3 ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    testArray(parser.parse(), Json.TRUE.getClass(), JsonNumber.class, JsonString.class,
        Json.NULL.getClass(), JsonNumber.class);
  }

  @Test public void testString_1() throws IOException, SyntaxError {
    String json = "[ \"hello\" ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertTrue(array.getElement(0) instanceof JsonString);
    JsonString string = (JsonString) array.getElement(0);
    assertEquals("hello", string.value);
  }

  @Test public void testNumber_1() throws IOException, SyntaxError {
    String json = "[ 0 ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertTrue(array.getElement(0) instanceof JsonNumber);
    JsonNumber number = (JsonNumber) array.getElement(0);
    assertEquals("0", number.value);
  }

  @Test public void testNumber_2() throws IOException, SyntaxError {
    String json = "[ -13 ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertTrue(array.getElement(0) instanceof JsonNumber);
    JsonNumber number = (JsonNumber) array.getElement(0);
    assertEquals("-13", number.value);
  }

  @Test public void testTrue() throws IOException, SyntaxError {
    String json = "[ true ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertSame(Json.TRUE, array.getElement(0));
  }

  @Test public void testFalse() throws IOException, SyntaxError {
    String json = "[ false ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertSame(Json.FALSE, array.getElement(0));
  }

  @Test public void testNull() throws IOException, SyntaxError {
    String json = "[ null ]";
    ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
    JsonParser parser = new JsonParser(in);
    JsonArray array = (JsonArray) parser.parse();
    assertSame(Json.NULL, array.getElement(0));
  }

  @Test public void testMissingArrayElement_1() throws IOException {
    try {
      String json = "[ false, ]";
      ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
      JsonParser parser = new JsonParser(in);
      assertTrue(parser.parse() instanceof JsonArray);
      fail("expected syntax error!");
    } catch (SyntaxError e) {
      assertEquals("Syntax Error: missing element in array", e.getMessage());
    }
  }

  @Test public void testMissingArrayElement_2() throws IOException, SyntaxError {
    try {
      String json = "[ , false ]";
      ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
      JsonParser parser = new JsonParser(in);
      assertTrue(parser.parse() instanceof JsonArray);
      fail("expected syntax error!");
    } catch (SyntaxError e) {
      assertEquals("Syntax Error: missing element in array", e.getMessage());
    }
  }

  @Test public void testMissingArrayElement_3() throws IOException, SyntaxError {
    try {
      String json = "[ , ]";
      ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
      JsonParser parser = new JsonParser(in);
      assertTrue(parser.parse() instanceof JsonArray);
      fail("expected syntax error!");
    } catch (SyntaxError e) {
      assertEquals("Syntax Error: missing element in array", e.getMessage());
    }
  }

  @Test public void testTrailingGarbage_1() throws IOException, SyntaxError {
    try {
      String json = "[ ] x";
      ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
      JsonParser parser = new JsonParser(in);
      assertTrue(parser.parse() instanceof JsonArray);
      fail("expected syntax error!");
    } catch (SyntaxError e) {
      assertEquals("Syntax Error: garbage at end of input (unexpected 'x')", e.getMessage());
    }
  }

  @Test public void testTrailingGarbage_2() throws IOException, SyntaxError {
    try {
      String json = "[ ] [ ]";
      ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
      JsonParser parser = new JsonParser(in);
      assertTrue(parser.parse() instanceof JsonArray);
      fail("expected syntax error!");
    } catch (SyntaxError e) {
      assertEquals("Syntax Error: garbage at end of input (unexpected '[')", e.getMessage());
    }
  }

  private static void testArray(JsonValue value, Class<?>... elementTypes) {
    assertTrue(value instanceof JsonArray);
    JsonArray array = (JsonArray) value;
    assertEquals(elementTypes.length, array.getNumElement());
    for (int i = 0; i < elementTypes.length; ++i) {
      assertEquals(elementTypes[i], array.getElement(i).getClass());
    }
  }

}
