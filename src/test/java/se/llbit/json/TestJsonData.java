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
package se.llbit.json;

import org.jastadd.util.PrettyPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import se.llbit.json.JsonParser.SyntaxError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestJsonData {
  private static String SYS_LINE_SEP = System.getProperty("line.separator");
  private static final String DATA_DIR = "testfiles";
  private final String jsonFilename;

  public TestJsonData(String jsonFilename) {
    this.jsonFilename = jsonFilename;
  }

  @Test public void runTest() throws IOException, SyntaxError {
    File jsonFile = new File(DATA_DIR, jsonFilename);
    FileInputStream in = new FileInputStream(jsonFile);
    JsonParser parser = new JsonParser(in);
    JsonValue json = parser.parse();
    in.close();
    File outFile = new File(DATA_DIR, jsonFilename + ".out");
    FileOutputStream out = new FileOutputStream(outFile);
    PrintStream outPs = new PrintStream(out);
    PrettyPrinter pp = new PrettyPrinter("  ", outPs);
    json.prettyPrint(pp);
    outPs.close();
    out.close();

    assertEquals("Output differs from expected output", readFileToString(jsonFile),
        readFileToString(outFile));
  }

  /**
   * Reads an entire file to a string object.
   *
   * <p>If the file does not exist an empty string is returned.
   *
   * <p>The system dependent line separator char sequence is replaced by
   * the newline character.
   */
  private static String readFileToString(File file) throws FileNotFoundException {
    if (!file.isFile()) {
      return "";
    }

    Scanner scanner = new Scanner(file);
    scanner.useDelimiter("\\Z");
    String theString = scanner.hasNext() ? scanner.next() : "";
    theString = theString.replace(SYS_LINE_SEP, "\n").trim();
    scanner.close();
    return theString;
  }

  @Parameters(name = "{0}") public static Iterable<Object[]> getTests() {
    Collection<Object[]> tests = new LinkedList<Object[]>();
    File dataDir = new File(DATA_DIR);
    if (dataDir.isDirectory()) {
      File[] files = new File(DATA_DIR).listFiles();
      for (File file : files) {
        if (file.isFile() && file.getName().endsWith(".json")) {
          tests.add(new Object[] {file.getName()});
        }
      }
    }
    return tests;
  }
}
