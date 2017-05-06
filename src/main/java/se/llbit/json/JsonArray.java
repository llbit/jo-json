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

import org.jastadd.util.PrettyPrintable;
import org.jastadd.util.PrettyPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON array. Elements are stored in a list.
 */
public class JsonArray extends JsonValue {
  List<JsonValue> elements = new ArrayList<JsonValue>();

  public void prettyPrint(PrettyPrinter out) {
    if (hasElement()) {
      out.print("[");
      out.println();
      out.indent(1);
      boolean first = true;
      for (PrettyPrintable p : getElementList()) {
        if (!first) {
          out.print(",");
          out.println();
        }
        first = false;
        out.print(p);
      }
      out.println();
      out.print("]");
    } else {
      out.print("[]");
    }
  }

  /**
   * Append an element to this array.
   *
   * @param value the value to add to the array.
   * @throws NullPointerException if {@code value} is null.
   */
  public void add(JsonValue value) {
    if (value == null) {
      throw new NullPointerException();
    }
    elements.add(value);
  }

  /**
   * Append a JSON string to this array.
   */
  public void add(String value) {
    elements.add(new JsonString(value));
  }

  /**
   * Append a JSON number to this array.
   */
  public void add(long value) {
    elements.add(new JsonNumber(value));
  }

  /**
   * Append a JSON number to this array.
   */
  public void add(double value) {
    elements.add(new JsonNumber(value));
  }

  /**
   * Append a JSON boolean to this array.
   */
  public void add(boolean value) {
    elements.add(value ? Json.TRUE : Json.FALSE);
  }

  /**
   * Get the element at the given index.
   */
  public JsonValue get(int index) {
    if (index >= 0 && index < elements.size()) {
      return elements.get(index);
    } else {
      return Json.UNKNOWN;
    }
  }

  public String toCompactString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    boolean first = true;
    for (JsonValue element : getElementList()) {
      if (!first) {
        sb.append(",");
      }
      first = false;
      sb.append(element.toCompactString());
    }
    sb.append("]");
    return sb.toString();
  }

  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    boolean first = true;
    for (JsonValue element : getElementList()) {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append(element.toString());
    }
    sb.append(" ]");
    return sb.toString();
  }

  /**
   * Gives the current number of elements in this array.
   */
  public int getNumElement() {
    return elements.size();
  }

  /**
   * Determine if this array has any elements.
   *
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   */
  public boolean hasElement() {
    return !elements.isEmpty();
  }

  /**
   * Replaces the element at index {@code i} with the new value {@code value}.
   *
   * @param value the value to replace the old element.
   * @param i array index of the node to be replaced.
   */
  public void set(int i, JsonValue value) {
    elements.set(i, value);
  }

  /**
   * Retrieves the Element list.
   *
   * @return a mutable reference to the internal element list of this array.
   */
  public List<JsonValue> getElementList() {
    return elements;
  }

  public boolean isArray() {
    return true;
  }

  @Override public JsonArray array() {
    return this;
  }

  @Override public JsonArray asArray() {
    return this;
  }
  public boolean isEmpty() {
    return !hasElement();
  }
}
