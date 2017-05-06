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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a JSON object.
 *
 * <p>Members are stored in a list.
 */
public class JsonObject extends JsonValue implements Iterable<JsonMember> {
  private final List<JsonMember> members = new ArrayList<JsonMember>();

  public void prettyPrint(PrettyPrinter out) {
    if (!isEmpty()) {
      out.print("{");
      out.println();
      out.indent(1);
      {
        boolean first = true;
        for (PrettyPrintable p : members) {
          if (!first) {
            out.print(",");
            out.println();
          }
          first = false;
          out.print(p);
        }
      }
      out.println();
      out.print("}");
    } else {
      out.print("{}");
    }
  }

  /**
   * Modify first member with the given name, or add a new member.
   */
  public void set(String name, JsonValue value) {
    for (int i = 0; i < size(); ++i) {
      if (getMember(i).getName().equals(name)) {
        members.set(i, new JsonMember(name, value));
        return;
      }
    }
    add(name, value);
  }

  /**
   * Add a member to the this JSON object.
   *
   * @throws NullPointerException if {@code value} is null.
   */
  public void add(String name, JsonValue value) {
    if (value == null) {
      throw new NullPointerException();
    }
    add(new JsonMember(name, value));
  }

  public void add(String name, String value) {
    add(name, new JsonString(value));
  }

  public void add(String name, long value) {
    add(name, new JsonNumber(value));
  }

  public void add(String name, double value) {
    add(name, new JsonNumber(value));
  }

  public void add(String name, boolean value) {
    add(name, value ? Json.TRUE : Json.FALSE);
  }

  public String toCompactString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    boolean first = true;
    for (JsonMember member : members) {
      if (!first) {
        sb.append(",");
      }
      first = false;
      sb.append(member.toCompactString());
    }
    sb.append("}");
    return sb.toString();
  }

  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    boolean first = true;
    for (JsonMember member : members) {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append(member.toString());
    }
    sb.append(" }");
    return sb.toString();
  }

  /**
   * Compute the number of members in this JSON object.
   */
  public int size() {
    return members.size();
  }

  /**
   * Retrieves the member at index {@code i}.
   *
   * @param i index of the member to return.
   * @throws IndexOutOfBoundsException if the given index is not valid.
   */
  public JsonMember getMember(int i) {
    return members.get(i);
  }

  /**
   * Append an element to the Member list.
   *
   * @param node The element to append to the Member list.
   */
  public void add(JsonMember node) {
    members.add(node);
  }

  /**
   * Build a map associating the member names to member values.
   */
  public Map<String, JsonValue> toMap() {
    Map<String, JsonValue> map = new HashMap<String, JsonValue>();
    for (JsonMember member : members) {
      if (!map.containsKey(member.getName())) {
        // Only the first occurrence of a member is mapped.
        map.put(member.getName(), member.getValue());
      }
    }
    return map;
  }

  /**
   * Retrieves the value of a member in this JSON object.
   *
   * @param name the name to search for
   * @return the JSON member value for the first member with the given name
   */
  public JsonValue get(String name) {
    for (JsonMember member : members) {
      if (member.getName().equals(name)) {
        return member.getValue();
      }
    }
    return Json.UNKNOWN;
  }

  public boolean isObject() {
    return true;
  }

  @Override public JsonObject object() {
    return this;
  }

  @Override public JsonObject asObject() {
    return this;
  }

  public boolean isEmpty() {
    return members.isEmpty();
  }

  @Override public Iterator<JsonMember> iterator() {
    return members.iterator();
  }
}
