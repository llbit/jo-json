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

/** Stores a JSON number as a string. */
public class JsonNumber extends JsonValue {
  public final String value;

  public JsonNumber(long value) {
    this.value = Long.toString(value);
  }

  public JsonNumber(double value) {
    this.value = Double.toString(value);
  }

  public JsonNumber(String value) {
    this.value = value;
  }

  public void prettyPrint(PrettyPrinter out) {
    out.print(value);
  }

  public String toCompactString() {
    return value;
  }

  @Override public String toString() {
    return value;
  }

  @Override public int intValue(int undefined) {
    return Integer.valueOf(value);
  }

  @Override public int asInt(int undefined) {
    return Integer.valueOf(value);
  }

  @Override public long longValue(long undefined) {
    return Long.valueOf(value);
  }

  @Override public long asLong(long undefined) {
    return Long.valueOf(value);
  }

  @Override public float floatValue(float undefined) {
    return Float.valueOf(value);
  }

  @Override public float asFloat(float undefined) {
    return Float.valueOf(value);
  }

  @Override public double doubleValue(double undefined) {
    return Double.valueOf(value);
  }

  @Override public double asDouble(double undefined) {
    return Double.valueOf(value);
  }

  @Override public JsonNumber copy() {
    return this;
  }

  @Override public int hashCode() {
    return value.hashCode();
  }

  /**
   * @return {@code true} if the argument object is a JsonNumber with
   * equal value string as this number.
   */
  @Override public boolean equals(Object obj) {
    if (!(obj instanceof JsonNumber)) {
      return false;
    }
    JsonNumber other = (JsonNumber) obj;
    return value == other.value || value.equals(other.value);
  }
}
