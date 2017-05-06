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

public class TestObject {
  @Test public void testSet() {
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
  }

  @Test(expected = NullPointerException.class)
  public void testAddErr() {
    new JsonObject().add("foo", (JsonValue) null);
  }

  @Test public void testObjectConversion() {
    JsonObject object = new JsonObject();
    assertSame(object, object.object());
    assertSame(object, object.asObject());
    assertNotSame(object, Json.of("bort").object());
    assertNotSame(object, object.asArray().object());
    assertNotSame(object, object.array().asObject());
  }
}
