package se.llbit.json;

import java.io.IOException;
import java.io.InputStream;

import se.llbit.io.LookaheadReader;

public class JsonParser {

	public static class SyntaxError extends Exception {
		public SyntaxError(String message) {
			super("Syntax Error: " + message);
		}
	}

	private final LookaheadReader in;

	public JsonParser(InputStream input) {
		in = new LookaheadReader(input, 8);
	}

	public JsonValue parse() throws IOException, SyntaxError {
		skipWhitespace();
		if (isObjectStart()) {
			return parseObject();
		} else if (isArrayStart()) {
			return parseArray();
		} else {
			throw new SyntaxError("expected object or array!");
		}
	}

	private JsonArray parseArray() throws IOException, SyntaxError {
		skipChar('[');
		JsonArray array = new JsonArray();
		boolean first = true;
		while (true) {
			skipWhitespace();
			JsonValue value = parseValue();
			if (value == null) {
				if (!first || in.peek(0) == ',') {
					throw new SyntaxError("missing element in array");
				}
				break;
			}
			array.addElement(value);
			skipWhitespace();
			if (in.peek(0) == ',') {
				first = false;
				skipChar(',');
			} else {
				break;
			}
		}
		skipChar(']');
		return array;
	}

	private JsonValue parseValue() throws IOException, SyntaxError {
		if (isObjectStart()) {
			return parseObject();
		} else if (isArrayStart()) {
			return parseArray();
		} else if (isNumber()) {
			return parseNumber();
		} else if (isString()) {
			return parseString();
		} else if (isTrue()) {
			in.skip(4);
			return new JsonTrue();
		} else if (isFalse()) {
			in.skip(5);
			return new JsonFalse();
		} else if (isNull()) {
			in.skip(4);
			return new JsonNull();
		} else {
			return null; // not a JSON value
		}
	}

	private JsonString parseString() throws IOException, SyntaxError {
		skipChar('"');
		StringBuilder sb = new StringBuilder();
		while (true) {
			int next = in.pop();
			if (next == -1) {
				throw new SyntaxError("EOF while parsing JSON string");
			} else if (next == '\\') {
				sb.append(unescapeStringChar());
			} else if (next == '"') {
				break;
			} else {
				sb.append((char) next);
			}
		}
		return new JsonString(sb.toString());
	}

	private char unescapeStringChar() throws IOException, SyntaxError {
		int next = in.pop();
		switch (next) {
		case '"':
		case '\\':
		case '/':
		case 'b':
		case 'f':
		case 'n':
		case 'r':
		case 't':
			return (char) next;
		case 'u':
			int u1 = hexdigit();
			int u2 = hexdigit();
			int u3 = hexdigit();
			int u4 = hexdigit();
			return (char) ((u1 << 12) | (u2 << 8) | (u3 << 4) | u4);
		case -1:
			throw new SyntaxError("EOF while parsing JSON string");
		default:
			throw new SyntaxError("illegal escape sequence in JSON string");
		}
	}

	private int hexdigit() throws IOException, SyntaxError {
		int next = in.pop();
		if (next >= '0' && next <= '9') {
			return next - '0';
		} else if (next >= 'A' && next <= 'F') {
			return next - 'A' + 0xA;
		} else if (next >= 'a' && next <= 'f') {
			return next - 'a' + 0xA;
		} else {
			throw new SyntaxError("non-hexadecimal digit in unicode escape sequence");
		}
	}

	private JsonValue parseNumber() throws IOException, SyntaxError {
		StringBuilder sb = new StringBuilder();
		while (true) {
			int peek = in.peek(0);
			if (peek == -1) {
				throw new SyntaxError("EOF while parsing JSON number");
			} else if (isDigit(peek)) {
				sb.append((char) in.pop());
			} else {
				return new JsonNumber(sb.toString());
			}
		}
	}

	private boolean isDigit(int chr) {
		return (chr >= '0' && chr <= '9') || chr == '.' || chr == 'e' ||
			chr == 'E' || chr == '-' || chr == '+';
	}

	private void skipWhitespace() throws IOException {
		while (isWhitespace(in.peek(0))) {
			in.pop();
		}
	}

	private boolean isWhitespace(int chr) {
		return chr == 0x20 || chr == 0x09 || chr == 0x0A || chr == 0x0D;
	}

	private boolean isNumber() throws IOException {
		int peek = in.peek(0);
		return (peek >= '0' && peek <= '9') || peek == '-' || peek == '+';
	}

	private boolean isString() throws IOException {
		return in.peek(0) == '"';
	}

	private boolean isTrue() throws IOException {
		return in.peek(0) == 't' &&
			in.peek(1) == 'r' &&
			in.peek(2) == 'u' &&
			in.peek(3) == 'e';
	}

	private boolean isFalse() throws IOException {
		return in.peek(0) == 'f' &&
			in.peek(1) == 'a' &&
			in.peek(2) == 'l' &&
			in.peek(3) == 's' &&
			in.peek(4) == 'e';
	}

	private boolean isNull() throws IOException {
		return in.peek(0) == 'n' &&
			in.peek(1) == 'u' &&
			in.peek(2) == 'l' &&
			in.peek(3) == 'l';
	}

	private JsonObject parseObject() throws IOException, SyntaxError {
		skipChar('{');
		JsonObject object = new JsonObject();
		boolean first = true;
		while (true) {
			skipWhitespace();
			JsonMember member = parseMember();
			if (member == null) {
				if (!first || in.peek(0) == ',') {
					throw new SyntaxError("missing member in object");
				}
				break;
			}
			object.addMember(member);
			skipWhitespace();
			if (in.peek(0) == ',') {
				first = false;
				skipChar(',');
			} else {
				break;
			}
		}
		skipChar('}');
		return object;
	}

	private void skipChar(char c) throws IOException, SyntaxError {
		int next = in.pop();
		if (next == -1) {
			throw new SyntaxError("encountered EOF while expecting '" + c + "'");
		}
		if (next != c) {
			throw new SyntaxError("unexpected character (was '" +
				((char) next) + "', expected '" + c + "')");
		}
	}

	private JsonMember parseMember() throws IOException, SyntaxError {
		if (isString()) {
			JsonString name = parseString();
			skipWhitespace();
			skipChar(':');
			skipWhitespace();
			JsonValue value = parseValue();
			if (value == null) {
				throw new SyntaxError("missing value for object member");
			}
			return new JsonMember(name.getValue(), value);
		} else {
			return null;
		}
	}

	private boolean isObjectStart() throws IOException {
		return in.peek(0) == '{';
	}

	private boolean isArrayStart() throws IOException {
		return in.peek(0) == '[';
	}
}
