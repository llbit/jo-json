/* Copyright (c) 2010-2013, Jesper Öqvist <jesper@cs.lth.se>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package se.llbit.io;
import java.io.*;

/**
 * @author Jesper Öqvist <jesper@llbit.se>
 */
public class LookaheadReader extends FilterReader {

	/**
	 * Maximum read buffer size
	 */
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * The lookahead
	 */
	private final int lookahead;
	
	private char[] buffer = new char[BUFF_SIZE];
	
	/**
	 * Current position in the read buffer
	 */
	private int pos = 0;
	
	/**
	 * Number of valid characters in the read buffer
	 */
	private int length = 0;

	/**
	 * Create a new lookahead reader
	 * @param in
	 * @param nlook
	 */
	public LookaheadReader(Reader in, int nlook) {
		super(in);
		lookahead = nlook;
		if (lookahead >= BUFF_SIZE) {
			throw new IllegalArgumentException("Too large lookahead");
		}
	}

	/**
	 * Create a new lookahead reader
	 * @param in
	 * @param nlook
	 */
	public LookaheadReader(InputStream in, int nlook) {
		this(new InputStreamReader(in), nlook);
	}

	/**
	 * Skip some input.
	 * @param num Number of characters to skip forward
	 */
	public void consume(int num) {
		pos += num;
	}

	/**
	 * Look ahead in the input stream.
	 * @param index Number of characters to look ahead
	 * @return The character at the given position, or -1 if the given
	 * position is outside the read buffer
	 * @throws IOException 
	 */
	public int peek(int index) throws IOException {
		refill();
		if ((pos+index) < length)
			return buffer[pos+index];
		else
			return -1;
	}

	/**
	 * Pop next character in the stream.
	 * @return The next character, or -1 if the end has been reached
	 * @throws IOException 
	 */
	public int pop() throws IOException {
		refill();
		if (pos < length)
			return buffer[pos++];
		else
			return -1;
	}

	/**
	 * Refills the input buffer if it does not currently satisfy the required
	 * lookahead.
	 */
	private void refill() throws IOException {
		if (length - pos <= lookahead) {
			if (length-pos > 0) {
				System.arraycopy(buffer, pos, buffer, 0, length-pos);
				length = length-pos;
				pos = 0;
			} else {
				length = 0;
				pos = 0;
			}
			int i = super.read(buffer, length, BUFF_SIZE-length);
			length += (i != -1) ? i : 0;
		}
	}

	@Override
	public int read() throws java.io.IOException {
		return pop();
	}

	@Override
	public int read(char cbuf[], int off, int len) throws IOException {
		if (!ready())
			return -1;
		len += off;

		for (int i=off; i<len; i++) {
			int c = read();
			if (c < 0) return i-off;
			else cbuf[i] = (char) c;
		}
		return len-off;
	}

	@Override
	public boolean ready() throws IOException {
		return pos < length || super.ready();
	}

}

