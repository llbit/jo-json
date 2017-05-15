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
package se.llbit.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LookaheadReader extends FilterReader {

  /** Maximum read buffer size. */
  private static final int BUFF_SIZE = 1024;

  /** The current lookahead. */
  private final int lookahead;

  private final char[] buffer = new char[BUFF_SIZE];

  /** Current position in the read buffer. */
  private int pos = 0;

  /** Number of characters in the read buffer. */
  private int length = 0;

  public LookaheadReader(Reader in, int lookahead) {
    super(in);
    this.lookahead = lookahead;
    if (this.lookahead >= BUFF_SIZE) {
      throw new IllegalArgumentException("Too large lookahead");
    }
  }

  public LookaheadReader(InputStream in, int lookahead) {
    this(new InputStreamReader(in), lookahead);
  }

  @Override public long skip(long num) throws IOException {
    refill();
    long skipped = Math.min(num, length - pos);
    pos += skipped;
    return skipped;
  }

  /**
   * @return The next character, or -1 if the next character
   * is past the end of the input stream
   */
  public int peek() throws IOException {
    refill();
    if (pos < length) {
      return buffer[pos];
    } else {
      return -1;
    }
  }

  /**
   * Look ahead in the input stream.
   *
   * @param index Number of characters to look ahead
   * @return The character at the given position, or -1 if the given
   * position is outside the read buffer
   */
  public int peek(int index) throws IOException {
    refill();
    if ((pos + index) < length) {
      return buffer[pos + index];
    } else {
      return -1;
    }
  }

  /**
   * Pop next character in the stream.
   *
   * @return The next character, or -1 if the end has been reached
   */
  public int pop() throws IOException {
    refill();
    if (pos < length) {
      return buffer[pos++];
    } else {
      return -1;
    }
  }

  /**
   * Refills the input buffer if it does not currently satisfy the required
   * lookahead.
   */
  private void refill() throws IOException {
    if (length - pos <= lookahead) {
      if (length - pos > 0) {
        System.arraycopy(buffer, pos, buffer, 0, length - pos);
        length = length - pos;
        pos = 0;
      } else {
        length = 0;
        pos = 0;
      }
      int i = super.read(buffer, length, BUFF_SIZE - length);
      length += (i != -1) ? i : 0;
    }
  }

  @Override public int read() throws java.io.IOException {
    return pop();
  }

  @Override public int read(char cbuf[], int offset, int len) throws IOException {
    if (!ready()) {
      return -1;
    }

    for (int i = 0; i < len; i++) {
      int c = read();
      if (c < 0) {
        return i;
      } else {
        cbuf[offset + i] = (char) c;
      }
    }
    return len;
  }

  @Override public boolean ready() throws IOException {
    return pos < length || super.ready();
  }

}

