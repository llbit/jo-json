package se.llbit.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestToString {

  @Test public void testNumber() {
    assertEquals("123", new JsonNumber("123").toString());
  }

  @Test public void testString() {
    assertEquals("\"eat more carrots\"", new JsonString("eat more carrots").toString());
  }

  @Test public void testTrue() {
    assertEquals("true", Json.TRUE.toString());
  }

  @Test public void testMember() {
    assertEquals("\"foo\" : \"bar\"", new JsonMember("foo", new JsonString("bar")).toString());
  }

  @Test public void testNull() {
    assertEquals("null", Json.NULL.toString());
  }

  @Test public void testObject() {
    JsonObject obj = new JsonObject();
    obj.addMember(new JsonMember("foo", new JsonString("bar")));
    obj.addMember(new JsonMember("cow", new JsonNumber("4")));
    assertEquals("{ \"foo\" : \"bar\", \"cow\" : 4 }", obj.toString());
  }

  @Test public void testArray() {
    JsonArray array = new JsonArray();
    array.addElement(new JsonString("foo"));
    array.addElement(new JsonString("bar"));
    array.addElement(Json.TRUE);
    array.addElement(new JsonNumber("4"));
    assertEquals("[ \"foo\", \"bar\", true, 4 ]", array.toString());
  }
}
