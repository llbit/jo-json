Simple Java JSON Library
========================

[![Build Status](https://travis-ci.org/llbit/jo-json.svg?branch=master)](https://travis-ci.org/llbit/jo-json)
[![codecov](https://codecov.io/gh/llbit/jo-json/branch/master/graph/badge.svg)](https://codecov.io/gh/llbit/jo-json)

Small Java JSON library.

I made this library originally as an exercise in using JastAdd to build a JSON
library, however the library has been converted to a static Java project to get
rid of some of the JastAdd overhead (parent pointers, child vector, attribute
visit flags).

Copyright (c) 2013-2017, Jesper Öqvist

This project is provided under the Modified BSD License.
See the LICENSE file, in the same directory as this README file.

Requirements
------------

This library requires Java 7+ to build.

Examples
--------

Here is an example snippet showing how to parse a JSON input stream and
read the parsed data:

```
import se.llbit.json.*;
...

try (JsonParser parser = new JsonParser(inputStream)) {
  JsonValue json = parser.parse();
  for (JsonValue entry : json.asArray()) {
    JsonObject person = entry.asObject();
    String name = person.get("name").asString("name");
    int age = person.get("age").asInt(-1);
    System.out.format("%s is %d years old%n", name, age);
  }
}
```

The input for the above example could look something like this:

```
[ { "name": "Alice", "age": 10 }, { "name": "Bob", "age": -31 } ]
```

