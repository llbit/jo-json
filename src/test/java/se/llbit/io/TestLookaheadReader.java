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
package se.llbit.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestLookaheadReader {
  private static LookaheadReader lookaheadReader(int lookahead, String text) throws IOException {
    return new LookaheadReader(
        new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)),
        lookahead);
  }

  /** Too large lookahead size (>= 1024). */
  @Test(expected = IllegalArgumentException.class)
  public void testTooLargeLookahead() throws IOException {
    lookaheadReader(1024, "");
  }

  @Test public void testEmpty() throws IOException {
    assertTrue(lookaheadReader(0, "").peek() == -1);
    assertTrue(lookaheadReader(1, "").peek() == -1);
    assertTrue(lookaheadReader(2, "").peek() == -1);
    assertTrue(lookaheadReader(3, "").peek() == -1);
    assertTrue(lookaheadReader(4, "").peek() == -1);
    assertTrue(lookaheadReader(1023, "").peek() == -1);
  }

  @Test public void testPeek1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals('b', reader.peek());
    assertEquals('b', reader.peek(0));
    assertEquals('o', reader.peek(1));

    // The following assertions pass because the buffer is filled past the lookahead.
    assertEquals('r', reader.peek(2));
    assertEquals('t', reader.peek(3));
    assertEquals(-1, reader.peek(4));
  }

  /** Peeking past the lookahead buffer just gives -1. */
  @Test public void testPeek2() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals(-1, reader.peek(1025));
  }

  @Test public void testRead1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals('b', reader.read());
    assertEquals('o', reader.read());
  }

  @Test public void testBlockRead1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    char[] buf = new char[10];
    assertEquals(4, reader.read(buf, 1, 4));
    assertEquals('b', buf[1]);
    assertEquals('t', buf[4]);
  }

  /** Block read past the end. */
  @Test public void testBlockRead2() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals(4, reader.read(new char[10], 1, 10));
  }

  /** Block read past the end with huge read length. */
  @Test public void testBlockRead3() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals(4, reader.read(new char[10], 1, Integer.MAX_VALUE));
  }

  /** Block read from empty reader. */
  @Test public void testBlockRead4() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "");
    assertEquals(-1, reader.read(new char[10], 1, 10));
  }

  /** Block read from finished reader. */
  @Test public void testBlockRead5() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "abcdef");
    reader.skip(6);
    assertEquals(-1, reader.read(new char[10], 1, 10));
  }

  @Test public void testPop1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals('b', reader.pop());
    assertEquals('o', reader.pop());
  }

  /** Skip before read. */
  @Test public void testSkip1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    reader.skip(2);
    assertEquals('r', reader.peek(0));
    assertEquals('t', reader.peek(1));
  }

  /** Skip after read. */
  @Test public void testSkip2() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    assertEquals('b', reader.pop());
    reader.skip(2);
    assertEquals('t', reader.pop());
  }

  /** Actual skipped characters is returned. */
  @Test public void testSkip3() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    reader.pop();
    assertEquals(3, reader.skip(10));
  }

  @Test public void testReady1() throws IOException {
    LookaheadReader reader = lookaheadReader(2, "bort");
    reader.pop();
    assertTrue(reader.ready());
  }

  @Test public void testReady2() throws IOException {
    assertTrue(lookaheadReader(2, "bort").ready()); // Different coverage from testReady1.
    assertFalse(lookaheadReader(0, "").ready());
    assertFalse(lookaheadReader(4, "").ready());
  }
}
