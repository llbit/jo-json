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

import org.jastadd.util.PrettyPrinter;

public class JsonString extends JsonValue implements Cloneable {
  private final String value;

  public JsonString(String value) {
    this.value = value;
  }

  public void prettyPrint(PrettyPrinter out) {
    out.print("\"");
    out.print(escaped());
    out.print("\"");
  }

  public String toCompactString() {
    return "\"" + escaped() + "\"";
  }

  @Override public String toString() {
    return "\"" + getValue() + "\"";
  }

  public String getValue() {
    return value;
  }

  /**
   * @param undefined value returned if this is not a JSON string.
   * @return the JSON string value
   */
  public String stringValue(String undefined) {
    return value;
  }

  public String escaped() {
    return escape(value);
  }

  /**
   * @return an escaped version of the input string where
   * special chars are escaped with reverse solidus.
   */
  protected static String escape(String string) {
    for (int i = 0; i < string.length(); ) {
      int cp = string.codePointAt(i);
      i += Character.charCount(cp);
      switch (cp) {
        case '"':
        case '\\':
        case '\n':
        case '\r':
        case '\t':
        case '\b':
        case '\f':
          // These characters should be escaped.
          return doStringEscape(string);
      }
    }
    // Found no character that needs escaping.
    return string;
  }

  /**
   * @return an escaped version of the input string where
   * special chars are escaped with reverse solidus.
   */
  protected static String doStringEscape(String string) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < string.length(); ) {
      int cp = string.codePointAt(i);
      i += Character.charCount(cp);
      switch (cp) {
        case '"':
          sb.append("\\\"");
          break;
        case '\\':
          sb.append("\\\\");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\f':
          sb.append("\\f");
          break;
        default:
          sb.appendCodePoint(cp);
      }
    }
    return sb.toString();
  }
}
