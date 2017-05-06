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

public class TestCompactString {
  @Test public void test1() {
    JsonArray array = new JsonArray();
    array.add("!");
    array.add(711);
    JsonObject object = new JsonObject();
    object.add(" ab cd", 123);
    object.add("@", "''''");
    object.add("\"\"", "\n\r");
    object.add(".", array);

    assertEquals("{\" ab cd\":123,\"@\":\"''''\",\"\\\"\\\"\":\"\\n\\r\",\".\":[\"!\",711]}",
        object.toCompactString());
  }

  @Test public void testBool() {
    JsonArray array = new JsonArray();
    array.add(true);
    array.add(false);
    assertEquals("[true,false]", array.toCompactString());
  }

  @Test public void testUnknown() {
    JsonArray array = new JsonArray();
    array.add(Json.UNKNOWN);
    assertEquals("[\"<unknown>\"]", array.toCompactString());
  }

  @Test public void testNull() {
    JsonArray array = new JsonArray();
    array.add(Json.NULL);
    assertEquals("[null]", array.toCompactString());
  }
}
