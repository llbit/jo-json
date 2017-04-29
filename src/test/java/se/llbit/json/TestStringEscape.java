package se.llbit.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringEscape {
  @Test public void testUnknown_1() {
    assertEquals("hello\\n", new JsonString("hello\n").escaped());
  }
}
