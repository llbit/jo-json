/* Copyright (c) 2017, Jesper Öqvist
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for hash codes and equals on JSON objects.
 */
public class TestComparison {
  @Test public void testArray1() {
    JsonArray a1 = new JsonArray();
    a1.add("!");
    a1.add(711);
    a1.add(0.2);
    a1.add(0.3);

    JsonArray a2 = new JsonArray();
    a2.add("!");
    a2.add(711);
    a2.add(0.2);
    a2.add(0.3);

    assertEquals(a1.hashCode(), a2.hashCode());
    assertEquals(a1, a2);
  }

  @Test public void testArray2() {
    JsonArray a1 = new JsonArray();
    a1.add("!");
    a1.add(true);
    a1.add(0.2);
    a1.add(0.3);

    JsonArray a2 = new JsonArray();
    a2.add("!");
    a2.add(false);
    a2.add(0.2);
    a2.add(0.3);

    // The hashes are not guaranteed different in this case, but very likely are.
    assertNotEquals(a1, a2);
  }

  /** Empty arrays are equal. */
  @Test public void testArray3() {
    JsonArray a1 = new JsonArray();
    JsonArray a2 = new JsonArray();
    assertEquals(a1.hashCode(), a2.hashCode());
    assertEquals(a1, a2);
  }

  /** Not same order. */
  @Test public void testArray4() {
    JsonArray a1 = new JsonArray();
    a1.add(0.3);
    a1.add(0.2);

    JsonArray a2 = new JsonArray();
    a2.add(0.2);
    a2.add(0.3);

    // Hash code is not affected by order:
    assertEquals(a1.hashCode(), a2.hashCode());
    assertNotEquals(a1, a2);
  }

  /** Duplicate element. */
  @Test public void testArray5() {
    JsonArray a1 = new JsonArray();
    a1.add(0.3);
    a1.add(0.2);

    JsonArray a2 = new JsonArray();
    a1.add(0.3);
    a2.add(0.2);
    a2.add(0.2);

    // The hashes are not guaranteed different in this case, but very likely are.
    assertNotEquals(a1, a2);
  }

  @Test public void testObject1() {
    JsonObject o1 = new JsonObject();
    o1.add("x", "!");
    o1.add("y", 1);
    o1.add("z", false);

    JsonObject o2 = new JsonObject();
    o2.add("x", "!");
    o2.add("y", 1);
    o2.add("z", false);

    assertEquals(o1.hashCode(), o2.hashCode());
    assertEquals(o1, o2);
  }

  /** Not same order. */
  @Test public void testObject2() {
    JsonObject o1 = new JsonObject();
    o1.add("x", "!");
    o1.add("y", 1);
    o1.add("z", false);

    JsonObject o2 = new JsonObject();
    o2.add("x", "!");
    o2.add("z", false);
    o2.add("y", 1);

    // Hash code is not affected by order:
    assertEquals(o1.hashCode(), o2.hashCode());

    assertNotEquals(o1, o2);
  }

  /** Duplicate member. */
  @Test public void testObject3() {
    JsonObject o1 = new JsonObject();
    o1.add("x", "!");
    o1.add("z", false);
    o1.add("y", 1);

    JsonObject o2 = new JsonObject();
    o2.add("x", "!");
    o2.add("z", false);
    o2.add("y", 1);
    o2.add("y", 1);

    // The hashes are not guaranteed different in this case, but very likely are.
    assertNotEquals(o1, o2);
  }

  /** Empty objects are equal. */
  @Test public void testObject4() {
    JsonObject o1 = new JsonObject();
    JsonObject o2 = new JsonObject();
    assertEquals(o1.hashCode(), o2.hashCode());
    assertEquals(o1, o2);
  }

  /** Different member names. */
  @Test public void testObject5() {
    JsonObject o1 = new JsonObject();
    o1.add("x", "!");

    JsonObject o2 = new JsonObject();
    o2.add("y", "!");

    // The hashes are not guaranteed different in this case, but very likely are.
    assertNotEquals(o1, o2);
  }

  @Test public void testString() {
    String v = "foo";
    assertEquals(Json.of(v), Json.of(v)); // Reference equality fast pass.
    StringBuilder sb = new StringBuilder();
    sb.append("f");
    for (int i = 0; i < 2; ++i) {
      sb.append("o");
    }
    assertEquals(Json.of("foo"), new JsonString(sb.toString()));
    assertNotEquals(Json.of("f_oo"), Json.of("foo"));
  }

  @Test public void testNumber() {
    String v = "101";
    assertEquals(new JsonNumber(v), new JsonNumber(v)); // Reference equality fast pass.

    assertEquals(Json.of(123400 + 56), Json.of(123456));

    // The string values are compared - different in this case.
    assertNotEquals(Json.of(-10101), Json.of(-10101.0));
  }

  @Test public void testMember() {
    String v = "foo";
    StringBuilder sb = new StringBuilder();
    sb.append("f");
    for (int i = 0; i < 2; ++i) {
      sb.append("o");
    }
    assertEquals(new JsonMember(v, Json.of(v)),
        new JsonMember(v, Json.of(v))); // Reference equality fast pass.
    assertEquals(new JsonMember(sb.toString(), Json.of(v)),
        new JsonMember(v, Json.of(v))); // Reference equality fast pass.
    assertEquals(new JsonMember(sb.toString(), Json.of(sb.toString())),
        new JsonMember(v, Json.of(v))); // Not ref-eq.
    assertNotEquals(new JsonMember(sb.toString(), Json.of(sb.toString())),
        new JsonMember(v, Json.of("x"))); // Not ref-eq.
  }

  @Test public void testNotSameType1() {
    assertNotEquals(new JsonObject(), new JsonArray());
    assertNotEquals(new JsonArray(), Json.of("foo"));
    assertNotEquals(Json.of("foo"), Json.of(10));
    assertNotEquals(Json.of(true), Json.of(10));
    assertNotEquals(Json.of(100), Json.NULL);
    assertNotEquals(new JsonMember("x", Json.of(false)), Json.of(3.2));
  }
}
