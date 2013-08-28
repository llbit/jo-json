package tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;
import se.llbit.json.JsonArray;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonParser;
import se.llbit.json.JsonParser.SyntaxError;
import se.llbit.json.JsonString;

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
		assertTrue(parser.parse() instanceof JsonArray);
	}

	@Test
	public void testOneElementArray_2() throws IOException, SyntaxError {
		String json = "[ 12 ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		assertTrue(parser.parse() instanceof JsonArray);
	}

	@Test
	public void testStringLiteral_1() throws IOException, SyntaxError {
		String json = "[ \"hello\" ]";
		ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		JsonParser parser = new JsonParser(in);
		JsonArray array = (JsonArray) parser.parse();
		JsonString value = (JsonString) array.getValue(0);
		assertEquals("hello", value.getValue());
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
