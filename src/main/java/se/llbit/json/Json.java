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

import org.jastadd.util.PrettyPrinter;

public interface Json {
  /** The JSON True literal. */
  JsonLiteral TRUE = new JsonLiteral() {
    @Override public void prettyPrint(PrettyPrinter out) {
      out.print("true");
    }

    @Override public String toCompactString() {
      return "true";
    }

    @Override public String toString() {
      return "true";
    }

    @Override public boolean boolValue(boolean undefined) {
      return true;
    }
  };

  /** The JSON False literal. */
  JsonLiteral FALSE = new JsonLiteral() {
    @Override public void prettyPrint(PrettyPrinter out) {
      out.print("false");
    }

    @Override public String toCompactString() {
      return "false";
    }

    @Override public String toString() {
      return "false";
    }

    @Override public boolean boolValue(boolean undefined) {
      return false;
    }
  };

  /** The JSON Unknown literal. */
  JsonValue UNKNOWN = new JsonValue() {
    @Override public void prettyPrint(PrettyPrinter out) {
      out.print("<unknown>");
    }

    @Override public String toCompactString() {
      return "\"<unknown value>\"";
    }

    @Override public boolean isUnknown() {
      return true;
    }
  };

  /** The JSON Null literal. */
  JsonLiteral NULL = new JsonLiteral() {
    @Override public void prettyPrint(PrettyPrinter out) {
      out.print("null");
    }

    @Override public String toCompactString() {
      return "null";
    }

    @Override public String toString() {
      return "null";
    }
  };
}
