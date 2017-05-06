/* Copyright (c) 2017 Jesper Öqvist <jesper@llbit.se>
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.llbit.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestArray {
  /** The add method supports different input types. */
  @Test public void testAdd() {
    JsonArray array = new JsonArray();
    assertTrue(array.isEmpty());
    array.add("!");
    array.add(711);
    array.add(0xFF00FF00FF00L);
    array.add(0.2);
    array.add(0.3);
    array.add(true);
    array.add(false);
    array.add(Json.NULL);

    assertEquals("!", array.get(0).asString(""));
    assertEquals(711, array.get(1).asInt(117));
    assertEquals(0xFF00FF00FF00L, array.get(2).asLong(1010));
    assertEquals(0.2, array.get(3).asDouble(117), 0.00001);
    assertEquals(0.3f, array.get(4).asFloat(117), 0.00001f);
    assertEquals(true, array.get(5).boolValue(false));
    assertEquals(false, array.get(6).boolValue(true));
    assertSame(Json.NULL, array.get(7));
    assertFalse(array.isEmpty());
  }

  /** The set method can modify existing elements and insert out-of-order. */
  @Test public void testSet() {
    JsonArray array = new JsonArray();
    assertTrue(array.isEmpty());
    array.add("wrong");
    array.add("wrong");
    array.add("wrong");
    array.set(0, Json.of("!"));
    array.set(2, Json.of(0xFF00FF00FF00L));
    array.set(1, Json.of(711));
    array.add(Json.of(0.2));
    array.add(Json.of(0.3));
    array.add(Json.of(true));
    array.add(Json.of(false));
    array.add(Json.NULL);

    assertEquals("!", array.get(0).asString(""));
    assertEquals(711, array.get(1).asInt(117));
    assertEquals(0xFF00FF00FF00L, array.get(2).asLong(1010));
    assertEquals(0.2, array.get(3).asDouble(117), 0.00001);
    assertEquals(0.3f, array.get(4).asFloat(117), 0.00001f);
    assertEquals(true, array.get(5).boolValue(false));
    assertEquals(false, array.get(6).boolValue(true));
    assertSame(Json.NULL, array.get(7));
    assertFalse(array.isEmpty());
  }

  /** May not add a null JsonValue. */
  @Test(expected = NullPointerException.class)
  public void testAddErr() {
    new JsonArray().add((JsonValue) null);
  }

  /** The set method can not insert past the end of the array. */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetErr() {
    JsonArray array = new JsonArray();
    array.add("bort");
    array.set(0, Json.of(711)); // OK!
    array.set(1, Json.of(711)); // Error.
  }

  @Test public void testArrayConversion() {
    JsonArray array = new JsonArray();
    assertSame(array, array.array());
    assertSame(array, array.asArray());
    assertNotSame(array, Json.of("bort").array());
    assertNotSame(array, new JsonObject().asArray());
  }
}
