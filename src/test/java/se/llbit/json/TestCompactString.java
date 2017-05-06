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
