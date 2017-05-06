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

public class TestCopy {
  /** Modifications to a copy do not affect the original, and vice versa. */
  @Test public void testIndependentArrayCopy() {
    JsonArray original = new JsonArray();
    original.addAll(
        Json.of(1), Json.of(2), Json.of("hi"),
        Json.NULL, Json.UNKNOWN);

    JsonArray copy = original.copy();

    copy.add(Json.NULL);
    assertEquals(5, original.size()); // Adding to the copy did not affect the original.

    original.remove(0);
    assertEquals(6, copy.size()); // Removing from the original did not affect the copy.
  }


  /** Modifications to a copy do not affect the original, and vice versa. */
  @Test public void testIndependentObjectCopy() {
    JsonObject original = new JsonObject();
    original.addAll(
        new JsonMember("1", Json.of(1)),
        new JsonMember("1", Json.of(true)),
        new JsonMember("1", Json.of(false)));

    JsonObject copy = original.copy();

    copy.add(new JsonMember("2", Json.of(2)));
    assertEquals(3, original.size()); // Adding to the copy did not affect the original.

    original.remove(0);
    assertEquals(4, copy.size()); // Removing from the original did not affect the copy.
  }
}
