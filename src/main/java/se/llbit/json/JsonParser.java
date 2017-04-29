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

import se.llbit.io.LookaheadReader;

import java.io.IOException;
import java.io.InputStream;

import static se.llbit.json.JsonConstants.BEGIN_ARRAY;
import static se.llbit.json.JsonConstants.BEGIN_OBJECT;
import static se.llbit.json.JsonConstants.END_ARRAY;
import static se.llbit.json.JsonConstants.END_OBJECT;
import static se.llbit.json.JsonConstants.ESCAPE;
import static se.llbit.json.JsonConstants.FALSE;
import static se.llbit.json.JsonConstants.NAME_SEPARATOR;
import static se.llbit.json.JsonConstants.NULL;
import static se.llbit.json.JsonConstants.QUOTE_MARK;
import static se.llbit.json.JsonConstants.TRUE;
import static se.llbit.json.JsonConstants.VALUE_SEPARATOR;

/**
 * Parses JSON input.
 */
public class JsonParser implements AutoCloseable {

  /** JSON parsing syntax error. */
  public static class SyntaxError extends Exception {
    public SyntaxError(String message) {
      super("Syntax Error: " + message);
    }
  }


  private final LookaheadReader in;

  /**
   * Parse the JSON object from the given input.
   * <p/>
   * <p>The input stream is not closed after being used.
   */
  public JsonParser(InputStream input) {
    in = new LookaheadReader(input, 8);
  }

  /**
   * Parses a JSON object or array.
   *
   * @return either a JsonObject or JsonArray, not null.
   */
  public JsonValue parse() throws IOException, SyntaxError {
    JsonValue value;
    skipWhitespace();
    switch (in.peek()) {
      case BEGIN_OBJECT:
        value = parseObject();
        break;
      case BEGIN_ARRAY:
        value = parseArray();
        break;
      default:
        throw new SyntaxError("expected JSON object or array");
    }
    skipWhitespace();
    if (in.peek() != -1) {
      throw new SyntaxError(
          String.format("garbage at end of input (unexpected '%c')", (char) in.peek()));
    }
    return value;
  }

  private JsonArray parseArray() throws IOException, SyntaxError {
    accept(BEGIN_ARRAY);
    JsonArray array = new JsonArray();
    do {
      skipWhitespace();
      JsonValue value = parseValue();
      if (value == null) {
        if (array.hasElement() || in.peek() == VALUE_SEPARATOR) {
          throw new SyntaxError("missing element in array");
        }
        break;
      }
      array.addElement(value);
      skipWhitespace();
    } while (skip(VALUE_SEPARATOR));
    accept(END_ARRAY);
    return array;
  }

  private JsonValue parseValue() throws IOException, SyntaxError {
    switch (in.peek()) {
      case BEGIN_OBJECT:
        return parseObject();
      case BEGIN_ARRAY:
        return parseArray();
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case '-':
      case '+':
        return parseNumber();
      case QUOTE_MARK:
        return parseString();
      case 't':
        acceptLiteral(TRUE);
        return Json.TRUE;
      case 'f':
        acceptLiteral(FALSE);
        return Json.FALSE;
      case 'n':
        acceptLiteral(NULL);
        return Json.NULL;
      default:
        // TODO: return a Null Object instead.
        return null; // Not a JSON value.
    }
  }

  private JsonString parseString() throws IOException, SyntaxError {
    accept(QUOTE_MARK);
    StringBuilder sb = new StringBuilder();
    while (true) {
      int next = in.pop();
      if (next == -1) {
        throw new SyntaxError("EOF while parsing JSON string (expected '\"')");
      } else if (next == ESCAPE) {
        sb.append(unescapeStringChar());
      } else if (next == QUOTE_MARK) {
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
      case QUOTE_MARK:
      case ESCAPE:
      case '/':
        return (char) next;
      case 'b':
        return '\b';
      case 'f':
        return '\f';
      case 'n':
        return '\n';
      case 'r':
        return '\r';
      case 't':
        return '\t';
      case 'u':
        int[] u = {hexdigit(), hexdigit(), hexdigit(), hexdigit()};
        return (char) ((u[0] << 12) | (u[1] << 8) | (u[2] << 4) | u[3]);
      case -1:
        throw new SyntaxError("end of input while parsing JSON string");
      default:
        throw new SyntaxError("illegal escape sequence in JSON string");
    }
  }

  private int hexdigit() throws IOException, SyntaxError {
    int next = in.pop();
    int v1 = next - '0';
    int v2 = next - 'A' + 0xA;
    int v3 = next - 'a' + 0xA;
    if (v1 >= 0 && v1 <= 9) {
      return v1;
    }
    if (v2 >= 0xA && v2 <= 0xF) {
      return v2;
    }
    if (v3 >= 0xA && v3 <= 0xF) {
      return v3;
    }
    throw new SyntaxError("non-hexadecimal digit in unicode escape sequence");
  }

  private JsonValue parseNumber() throws IOException, SyntaxError {
    StringBuilder sb = new StringBuilder();
    while (true) {
      switch (in.peek()) {
        case -1:
          throw new SyntaxError("EOF while parsing JSON number");
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
        case '-':
        case '+':
        case '.':
        case 'e':
        case 'E':
          sb.append((char) in.pop());
          break;
        default:
          return new JsonNumber(sb.toString());
      }
    }
  }

  private void skipWhitespace() throws IOException {
    while (isWhitespace(in.peek())) {
      in.pop();
    }
  }

  private boolean isWhitespace(int chr) {
    return chr == 0x20 || chr == 0x09 || chr == 0x0A || chr == 0x0D;
  }

  private void acceptLiteral(char[] literal) throws IOException, SyntaxError {
    for (char c : literal) {
      if (in.pop() != c) {
        throw new SyntaxError("encountered invalid JSON literal");
      }
    }
  }

  private JsonObject parseObject() throws IOException, SyntaxError {
    accept(BEGIN_OBJECT);
    JsonObject object = new JsonObject();
    do {
      skipWhitespace();
      JsonMember member = parseMember();
      if (member == null) {
        if (object.hasMember() || in.peek() == VALUE_SEPARATOR) {
          throw new SyntaxError("missing member in object");
        }
        break;
      }
      object.addMember(member);
      skipWhitespace();
    } while (skip(VALUE_SEPARATOR));
    accept(END_OBJECT);
    return object;
  }

  private boolean skip(char c) throws IOException, SyntaxError {
    boolean skip = in.peek() == c;
    if (skip) {
      in.pop();
    }
    return skip;
  }

  private void accept(char c) throws IOException, SyntaxError {
    int next = in.pop();
    if (next == -1) {
      throw new SyntaxError(String.format("unexpected end of input (expected '%c')", c));
    }
    if (next != c) {
      throw new SyntaxError(
          String.format("unexpected character (was '%c', expected '%c')", (char) next, c));
    }
  }

  private JsonMember parseMember() throws IOException, SyntaxError {
    if (in.peek() == QUOTE_MARK) {
      JsonString name = parseString();
      skipWhitespace();
      accept(NAME_SEPARATOR);
      skipWhitespace();
      JsonValue value = parseValue();
      if (value == null) {
        throw new SyntaxError("missing value for object member");
      }
      return new JsonMember(name.getValue(), value);
    }
    return null;
  }

  @Override public void close() throws IOException {
    in.close();
  }
}
