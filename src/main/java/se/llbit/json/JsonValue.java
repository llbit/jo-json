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

/** Abstract base class for all JSON values. */
public abstract class JsonValue implements PrettyPrintable {
  protected JsonValue() { }

  /**
   * Produce a compact string representation of this JSON object.
   *
   * @return compact string
   */
  public abstract String toCompactString();

  public boolean isUnknown() {
    return false;
  }

  /** Test if this JSON value is a JSON object. */
  public boolean isObject() {
    return false;
  }

  /** Test if this JSON value is a JSON array. */
  public boolean isArray() {
    return false;
  }

  /**
   * Convert this JSON value to an object.
   */
  public JsonObject object() {
    return new JsonObject();
  }

  /**
   * Convert this JSON value to an object.
   */
  public JsonObject asObject() {
    return new JsonObject();
  }

  /**
   * Convert this JSON value to an array.
   */
  public JsonArray array() {
    return new JsonArray();
  }

  /**
   * Convert this JSON value to an array.
   */
  public JsonArray asArray() {
    return new JsonArray();
  }

  /**
   * The String value of this JSON string.
   * @param undefined value returned if this is not a JSON string.
   */
  public String stringValue(String undefined) {
    return undefined;
  }

  /**
   * The String value of this JSON string.
   * @param undefined value returned if this is not a JSON string.
   */
  public String asString(String undefined) {
    return undefined;
  }

  /**
   * The {@code int}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public int intValue(int undefined) {
    return undefined;
  }

  /**
   * The {@code int}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public int asInt(int undefined) {
    return undefined;
  }

  /**
   * The {@code long}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public long longValue(long undefined) {
    return undefined;
  }

  /**
   * The {@code long}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public long asLong(long undefined) {
    return undefined;
  }

  /**
   * The {@code float}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public float floatValue(float undefined) {
    return undefined;
  }

  /**
   * The {@code float}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public float asFloat(float undefined) {
    return undefined;
  }

  /**
   * The {@code double}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public double doubleValue(double undefined) {
    return undefined;
  }

  /**
   * The {@code double}-converted value of this JSON number.
   * @param undefined value returned if this is not a JSON number.
   */
  public double asDouble(double undefined) {
    return undefined;
  }

  /**
   * The {@code boolean} value of this JSON boolean.
   * @param undefined value returned if this is not a JSON boolean.
   */
  public boolean boolValue(boolean undefined) {
    return undefined;
  }

  /**
   * The {@code boolean} value of this JSON boolean.
   * @param undefined value returned if this is not a JSON boolean.
   */
  public boolean asBoolean(boolean undefined) {
    return undefined;
  }

  /**
   * Creates an independent copy of this JSON value.
   *
   * <p>If this JSON value is immutable, then this object is returned.
   * Otherwise, a fresh object containing the same data is created and returned.
   */
  public abstract JsonValue copy();
}
