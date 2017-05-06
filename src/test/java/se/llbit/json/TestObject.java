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
    assertFalse(Json.of("bort").isObject());
    assertFalse(object.array().isObject());
    assertNotSame(object, Json.of("bort").object());
    assertNotSame(object, object.asArray().object());
    assertNotSame(object, object.array().asObject());
  }

  @Test public void testIterator() {
    JsonObject array = new JsonObject();
    array.add("x", "!");
    array.add("y", 1);

    Iterator<JsonMember> iterator = array.iterator();
    assertEquals("x", iterator.next().name);
    assertEquals("y", iterator.next().name);
    assertFalse(iterator.hasNext());
  }
}
