package se.llbit.json;

import java.io.InputStream;

import org.jastadd.io.LookaheadReader;

public class JsonParser {

	public static class SyntaxError extends Exception {
		public SyntaxError(String message) {
			super(message);
		}
	}

	private final LookaheadReader in;

	public JsonParser(InputStream input) {
		in = new LookaheadReader(input, 8);
	}

	public JsonValue parse() {
		return null;
	}
}
