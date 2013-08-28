package tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;
import se.llbit.json.*;
import se.llbit.json.JsonParser.SyntaxError;

/**
 * Simple JSON parsing tests
 * @author Jesper Öqvist <jesper.oqvist@cs.lth.se>
 */
@SuppressWarnings("javadoc")
public class TestParsing {

	@Test
	public void testEmptyObject() throws IOException, SyntaxError {
		String json = " { } ";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		assertTrue(parser.parse() instanceof JsonObject);
	}

	@Test
	public void testOneMemberObject_1() throws IOException, SyntaxError {
		String json = " { \"a\": 0 } ";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonObject object = (JsonObject) parser.parse();
		assertEquals(1, object.getNumMember());
		assertTrue(object.getMember(0) instanceof JsonMember);
		assertTrue(object.getMember(0).getValue() instanceof JsonNumber);
	}

	@Test
	public void testNestedObjectArray_1() throws IOException, SyntaxError {
		String json = " { \"a\": [ 0, 1, 2 ] } ";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonObject object = (JsonObject) parser.parse();
		assertEquals(1, object.getNumMember());
		assertTrue(object.getMember(0) instanceof JsonMember);
		assertTrue(object.getMember(0).getValue() instanceof JsonArray);
		JsonArray array = (JsonArray) object.getMember(0).getValue();
		assertEquals(3, array.getNumValue());
		assertTrue(array.getValue(0) instanceof JsonNumber);
		assertTrue(array.getValue(1) instanceof JsonNumber);
		assertTrue(array.getValue(2) instanceof JsonNumber);
	}

	@Test
	public void testEmptyArray() throws IOException, SyntaxError {
		String json = "[]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		assertTrue(parser.parse() instanceof JsonArray);
	}

	@Test
	public void testOneElementArray_1() throws IOException, SyntaxError {
		String json = "[ false ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertEquals(1, array.getNumValue());
		assertTrue(array.getValue(0) instanceof JsonFalse);
	}

	@Test
	public void testOneElementArray_2() throws IOException, SyntaxError {
		String json = "[ 12 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertEquals(1, array.getNumValue());
		assertTrue(array.getValue(0) instanceof JsonNumber);
	}

	@Test
	public void testMultiElementArray_1() throws IOException, SyntaxError {
		String json = "[ 12, -3 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertEquals(2, array.getNumValue());
		assertTrue(array.getValue(0) instanceof JsonNumber);
		assertTrue(array.getValue(1) instanceof JsonNumber);
	}

	@Test
	public void testMultiElementArray_2() throws IOException, SyntaxError {
		String json = "[ true, 1000, \"a\", null, -3 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertEquals(5, array.getNumValue());
		assertTrue(array.getValue(0) instanceof JsonTrue);
		assertTrue(array.getValue(1) instanceof JsonNumber);
		assertTrue(array.getValue(2) instanceof JsonString);
		assertTrue(array.getValue(3) instanceof JsonNull);
		assertTrue(array.getValue(4) instanceof JsonNumber);
	}

	@Test
	public void testString_1() throws IOException, SyntaxError {
		String json = "[ \"hello\" ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonString);
		JsonString value = (JsonString) array.getValue(0);
		assertEquals("hello", value.getValue());
	}

	@Test
	public void testNumber_1() throws IOException, SyntaxError {
		String json = "[ 0 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonNumber);
		JsonNumber value = (JsonNumber) array.getValue(0);
		assertEquals("0", value.getValue());
	}

	@Test
	public void testNumber_2() throws IOException, SyntaxError {
		String json = "[ -13 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonNumber);
		JsonNumber value = (JsonNumber) array.getValue(0);
		assertEquals("-13", value.getValue());
	}

	@Test
	public void testTrue() throws IOException, SyntaxError {
		String json = "[ true ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonTrue);
	}

	@Test
	public void testFalse() throws IOException, SyntaxError {
		String json = "[ false ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonFalse);
	}

	@Test
	public void testNull() throws IOException, SyntaxError {
		String json = "[ null ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		assertTrue(array.getValue(0) instanceof JsonNull);
	}

	@Test
	public void testMissingArrayElement_1() throws IOException {
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

	@Test
	public void testMissingArrayElement_2() throws IOException, SyntaxError {
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

	@Test
	public void testMissingArrayElement_3() throws IOException, SyntaxError {
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

}
