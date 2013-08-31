package tests;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import org.jastadd.util.PrettyPrinter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import se.llbit.json.JsonValue;
import se.llbit.json.JsonParser;
import se.llbit.json.JsonParser.SyntaxError;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestJsonData {
	private static String SYS_LINE_SEP = System.getProperty("line.separator");
	private static final String DATA_DIR = "data";
	private final String jsonFilename;

	public TestJsonData(String jsonFilename) {
		this.jsonFilename = jsonFilename;
	}

	@Test
	public void runTest() throws IOException, SyntaxError {
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

		assertEquals("Output differs from expected output",
				readFileToString(jsonFile),
				readFileToString(outFile));
	}

	/**
	 * <p>Reads an entire file to a string object.
	 *
	 * <p>If the file does not exist an empty string is returned.
	 *
	 * <p>The system dependent line separator char sequence is replaced by
	 * the newline character.
	 *
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	private static String readFileToString(File file) throws FileNotFoundException {
		if (!file.isFile())
			return "";

		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\Z");
		String theString = scanner.hasNext() ? scanner.next() : "";
		theString = theString.replace(SYS_LINE_SEP, "\n").trim();
		scanner.close();
		return theString;
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		Collection<Object[]> tests = new LinkedList<Object[]>();
		File dataDir = new File(DATA_DIR);
		if (dataDir.isDirectory()) {
			File[] files = new File(DATA_DIR).listFiles();
			for (File file: files) {
				if (file.isFile() && file.getName().endsWith(".json")) {
					tests.add(new Object[] {file.getName()});
				}
			}
		}
		return tests;
	}
}
