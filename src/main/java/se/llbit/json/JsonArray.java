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

public class JsonArray extends JsonValue implements Cloneable {
  List<JsonValue> elements = new ArrayList<JsonValue>();

  public void prettyPrint(PrettyPrinter out) {
    if (hasElement()) {
      out.print("[");
      out.println();
      out.indent(1);
      {
        boolean first = true;
        for (PrettyPrintable p : getElementList()) {
          if (!first) {
            out.print(",");
            out.println();
          }
          first = false;
          out.print(p);
        }
      }
      out.println();
      out.print("]");
    } else {
      out.print("[]");
    }
  }

  public void add(JsonValue value) {
    elements.add(value);
  }

  public void add(String value) {
    elements.add(new JsonString(value));
  }

  public void add(long value) {
    elements.add(new JsonNumber(value));
  }

  public void add(double value) {
    elements.add(new JsonNumber(value));
  }

  public void add(boolean value) {
    elements.add(value ? Json.TRUE : Json.FALSE);
  }

  /**
   * Get the element at the given index.
   */
  public JsonValue get(int index) {
    if (index >= 0 && index < elements.size()) {
      return getElement(index);
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
   * Retrieves the number of children in the Element list.
   *
   * @return Number of children in the Element list.
   */
  public int getNumElement() {
    return elements.size();
  }

  /**
   * Retrieves the element at index {@code i} in the Element list.
   *
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Element list.
   */
  public JsonValue getElement(int i) {
    return elements.get(i);
  }

  /**
   * Check whether the Element list has any children.
   *
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   */
  public boolean hasElement() {
    return !elements.isEmpty();
  }

  /**
   * Append an element to the Element list.
   *
   * @param node The element to append to the Element list.
   */
  public void addElement(JsonValue node) {
    elements.add(node);
  }

  /**
   * Replaces the Element list element at index {@code i} with the new node {@code node}.
   *
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   */
  public void setElement(JsonValue node, int i) {
    elements.set(i, node);
  }

  /**
   * Retrieves the Element list.
   *
   * @return The node representing the Element list.
   */
  public List<JsonValue> getElementList() {
    return elements;
  }

  public boolean isArray() {
    return true;
  }

  public JsonArray array() {
    return this;
  }
}
