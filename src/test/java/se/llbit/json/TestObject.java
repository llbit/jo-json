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

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestObject {
  /** Members can be changed by index. */
  @Test public void testSet1() {
    JsonObject object = new JsonObject();
    assertTrue(object.isEmpty());
    object.add("bart", 10);
    object.add("bort", -10);
    object.set(0, Json.of(20));
    object.set(1, Json.of(12));
    assertEquals(20, object.get("bart").asInt(0));
    assertEquals(12, object.get("bort").asInt(0));
    assertFalse(object.isEmpty());
    assertEquals(2, object.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSet1Err1() {
    JsonObject object = new JsonObject();
    object.set(1, Json.of(12));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSet1Err2() {
    JsonObject object = new JsonObject();
    object.set(-1, Json.of(12));
  }

  /** Members can be changed by name. */
  @Test public void testSet2() {
    JsonObject object = new JsonObject();
    assertTrue(object.isEmpty());
    object.add("bart", 10);
    object.add("bort", -10);
    object.set("bort", Json.of(20));
    object.set("lisa", Json.of(12));
    assertEquals(10, object.get("bart").asInt(0));
    assertEquals(20, object.get("bort").asInt(0));
    assertEquals(12, object.get("lisa").asInt(0));
    assertFalse(object.isEmpty());
    assertEquals(3, object.size());
  }

  /** Multiple members can have the same name. */
  @Test public void testDuplicateAdd() {
    JsonObject object = new JsonObject();
    assertTrue(object.isEmpty());
    object.add("bart", 10);
    object.add("bort", -10);
    object.add("bort", Json.of(20));
    assertEquals(3, object.size());
  }

  @Test(expected = NullPointerException.class)
  public void testAddErr() {
    new JsonObject().add("foo", (JsonValue) null);
  }

  @Test public void testObjectConversion() {
    JsonObject object = new JsonObject();
    assertSame(object, object.object());
    assertSame(object, object.asObject());
    assertFalse(Json.of("bort").isObject());
    assertFalse(object.array().isObject());
    assertNotSame(object, Json.of("bort").object());
    assertNotSame(object, object.asArray().object());
    assertNotSame(object, object.array().asObject());
  }

  @Test public void testIterator() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    object.add("y", 1);

    Iterator<JsonMember> iterator = object.iterator();
    assertEquals("x", iterator.next().name);
    assertEquals("y", iterator.next().name);
    assertFalse(iterator.hasNext());
  }

  /** Member access by index. */
  @Test public void testGetMember1() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    object.add("y", 1);

    assertEquals("y", object.get(1).name);
    assertEquals("x", object.get(0).name);
  }

  /** Member access by name. */
  @Test public void testGetMember2() {
    JsonObject object = new JsonObject();
    object.add("z", "-");
    object.add("x", "!");
    object.add("y", 1);
    object.add("x", "?");

    assertEquals("!", object.get("x").stringValue(""));
  }

  /** Member index out of bounds raises an exception. */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetMemberErr1() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    object.add("y", 1);
    object.get(3);
  }

  /** Member index out of bounds raises an exception. */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetMemberErr2() {
    JsonObject object = new JsonObject();
    object.get(0);
  }

  /** Member index out of bounds raises an exception. */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetMemberErr3() {
    JsonObject object = new JsonObject();
    object.add("y", 1);
    object.get(-1);
  }

  /** Members can be removed by index. */
  @Test public void testRemoveMember1() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    object.add("y", 1);
    object.add("z", false);

    assertEquals(3, object.size());
    assertEquals("y", object.get(1).name);

    object.remove(1);
    assertEquals(2, object.size());
    assertEquals("z", object.get(1).name);
  }

  /** Members can be removed by name. This removes only the first occurrence. */
  @Test public void testRemoveMember2() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    object.add("u", 0);
    object.add("y", 1);
    object.add("u", 2);
    object.add("z", false);

    assertEquals(5, object.size());
    assertEquals(0, object.get("u").intValue(301));

    object.remove("u");
    assertEquals(4, object.size());
    assertEquals(2, object.get("u").intValue(301));

    object.remove("u");
    assertEquals(3, object.size());
    assertTrue(object.get("u").isUnknown());
    assertEquals(301, object.get("u").intValue(301));
  }

  /** The value returned by {@code remove(int)} is the removed member. */
  @Test public void testRemoveMember3() {
    JsonObject object = new JsonObject();
    object.add("x", "!");
    assertEquals("!", object.remove(0).value.asString(""));
  }

  /** The value returned by {@code remove(String)} is the removed member (or {@code null}). */
  @Test public void testRemoveMember4() {
    JsonObject object = new JsonObject();
    assertNull(object.remove("missing"));

    object.add("x", "!");
    assertEquals("!", object.remove("x").value.asString(""));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveMemberErr1() {
    JsonObject object = new JsonObject();
    object.remove(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveMemberErr2() {
    JsonObject object = new JsonObject();
    object.remove(-2);
  }

  @Test public void testAddAll() {
    JsonObject object = new JsonObject();
    object.addAll(
        new JsonMember("bart", Json.of(10)),
        new JsonMember("bort", Json.of(20)));
    assertEquals(2, object.size());
    assertEquals(10, object.get("bart").asInt(0));
    assertEquals(20, object.get("bort").asInt(0));

    // Test that the members are added in argument order.
    object.addAll(
        new JsonMember("lisa", Json.of(1)),
        new JsonMember("lisa", Json.of(2)));
    assertEquals(4, object.size());
    assertEquals(1, object.get("lisa").asInt(0));
  }

  /** It is okay to pass zero arguments to addAll. */
  @Test public void testAddAll2() {
    JsonObject object = new JsonObject();
    object.addAll();
    assertTrue(object.isEmpty());
  }

  /** Can't pass null to addAll. */
  @Test(expected = NullPointerException.class)
  public void testAddAllErr1() {
    JsonObject object = new JsonObject();
    object.addAll((JsonMember[]) null);
  }

  /** Can't pass null to addAll. */
  @Test(expected = NullPointerException.class)
  public void testAddAllErr2() {
    JsonObject object = new JsonObject();
    object.addAll((JsonMember) null);
  }
}
