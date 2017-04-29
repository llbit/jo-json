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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a JSON object.
 *
 * <p>Members are stored in a list.
 */
public class JsonObject extends JsonValue {
  private final List<JsonMember> members = new ArrayList<JsonMember>();

  public void prettyPrint(PrettyPrinter out) {
    if (hasMember()) {
      out.print("{");
      out.println();
      out.indent(1);
      {
        boolean first = true;
        for (PrettyPrintable p : getMemberList()) {
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
    for (int i = 0; i < getNumMember(); ++i) {
      if (getMember(i).getName().equals(name)) {
        members.set(i, new JsonMember(name, value));
        return;
      }
    }
    add(name, value);
  }

  /**
   * Add a member to the Json object.
   */
  public void add(String name, JsonValue value) {
    if (value != null) {
      addMember(new JsonMember(name, value));
    }
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
    for (JsonMember member : getMemberList()) {
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
    for (JsonMember member : getMemberList()) {
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
   * Retrieves the number of children in the Member list.
   *
   * @return Number of children in the Member list.
   */
  public int getNumMember() {
    return members.size();
  }

  /**
   * Retrieves the element at index {@code i} in the Member list.
   *
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Member list.
   */
  public JsonMember getMember(int i) {
    return members.get(i);
  }

  /**
   * Check whether the Member list has any children.
   *
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   */
  public boolean hasMember() {
    return !members.isEmpty();
  }

  /**
   * Append an element to the Member list.
   *
   * @param node The element to append to the Member list.
   */
  public void addMember(JsonMember node) {
    members.add(node);
  }

  /**
   * Retrieves the Member list.
   *
   * @return The node representing the Member list.
   */
  public List<JsonMember> getMemberList() {
    return members;
  }

  /**
   * Retrieves the Member list.
   *
   * @return The node representing the Member list.
   */
  public List<JsonMember> getMembers() {
    return members;
  }

  /**
   * Builds a map associating the member names to member values.
   */
  public Map<String, JsonValue> toMap() {
    Map<String, JsonValue> map = new HashMap<String, JsonValue>();
    for (JsonMember member : getMemberList()) {
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
    for (JsonMember member : getMemberList()) {
      if (member.getName().equals(name)) {
        return member.getValue();
      }
    }
    return Json.UNKNOWN;
  }

  public boolean hasMember(String name) {
    for (JsonMember member : getMemberList()) {
      if (member.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public boolean isObject() {
    return true;
  }

  public JsonObject object() {
    return this;
  }
}
