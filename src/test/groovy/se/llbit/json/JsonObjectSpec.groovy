package se.llbit.json

import spock.lang.*

class JsonObjectSpec extends Specification {
  def "A fresh new object is empty."() {
    when:
    JsonObject object = new JsonObject()

    then:
    object.isEmpty()
  }

  def "Members can be changed by set(index)."() {
    when:
    JsonObject object = new JsonObject()
    object.add("bart", 10)
    object.add("bort", -10)
    object.set(0, Json.of(20))
    object.set(1, Json.of(12))

    then:
    object.get("bart").asInt(0) == 20
    object.get("bort").asInt(0) == 12
    !object.isEmpty()
    object.size() == 2
  }

  def "IndexOutOfBoundsException thrown if set() argument is too large."() {
    when:
    JsonObject object = new JsonObject()
    object.set(1, Json.of(12))

    then:
    thrown IndexOutOfBoundsException
  }

  def "IndexOutOfBoundsException thrown if set() argument is negative."() {
    when:
    JsonObject object = new JsonObject()
    object.set(-1, Json.of("oh hi mark"))

    then:
    thrown IndexOutOfBoundsException
  }

  def "Members can be changed by name."() {
    when:
    JsonObject object = new JsonObject()
    object.add("bart", 10)
    object.add("bort", -10)
    object.set("bort", Json.of(20))
    object.set("lisa", Json.of(12))

    then:
    object.get("bart").asInt(0) == 10
    object.get("bort").asInt(0) == 20
    object.get("lisa").asInt(0) == 12
    !object.isEmpty()
    object.size() == 3
  }

  def "Multiple members can have the same name."() {
    when:
    JsonObject object = new JsonObject()
    object.add("bart", 10)
    object.add("mark", Json.of("oh hi"))
    object.add("mark", Json.of(20))

    then:
    object.size() == 3
    object.get("mark").asString() == "oh hi"
  }

  def "Can not add a null reference as a member."() {
    when:
    new JsonObject().add("foo", (JsonValue) null)

    then:
    thrown NullPointerException
  }

  def "Conversions"() {
    when:
    JsonObject object = new JsonObject()

    then:
    object.object().is(object)
    object.asObject().is(object)
    !Json.of("bort").isObject()
    !object.array().isObject()
    !Json.of("bort").object().is(object)
    !object.asArray().object().is(object)
    !object.array().asObject().is(object)
  }

  def "Iterator"() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")
    object.add("y", 1)
    Iterator<JsonMember> iterator = object.iterator()

    then:
    iterator.next().name == "x"
    iterator.next().name == "y"
    !iterator.hasNext()
  }

  def "Member access by index."() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")
    object.add("y", 1)

    then:
    object.get(1).name == "y"
    object.get(0).name == "x"
  }

  def "Member access by name."() {
    when:
    JsonObject object = new JsonObject()
    object.add("z", "-")
    object.add("x", "!")
    object.add("y", 1)
    object.add("x", "?")

    then:
    object.get("x").stringValue("") == "!"
  }

  def "Member index out of bounds raises an exception (1)."() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")
    object.add("y", 1)
    object.get(3)

    then:
    thrown IndexOutOfBoundsException
  }

  def "Member index out of bounds raises an exception (2)."() {
    when:
    JsonObject object = new JsonObject()
    object.get(0)

    then:
    thrown IndexOutOfBoundsException
  }

  def "Member index out of bounds raises an exception (3)."() {
    when:
    JsonObject object = new JsonObject()
    object.add("y", 1)
    object.get(-1)

    then:
    thrown IndexOutOfBoundsException
  }

  def "Members can be removed by index."() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")
    object.add("y", 1)
    object.add("z", false)

    then:
    object.size() == 3
    object.get(1).name == "y"

    when:
    object.remove(1)

    then:
    object.size() == 2
    object.get(1).name == "z"
  }

  def "Members can be removed by name. This removes only the first occurrence."() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")
    object.add("u", 0)
    object.add("y", 1)
    object.add("u", 2)
    object.add("z", false)

    then:
    object.size() == 5
    object.get("u").intValue(301) == 0

    when:
    object.remove("u")

    then:
    object.size() == 4
    object.get("u").intValue(301) == 2

    when:
    object.remove("u")

    then:
    object.size() == 3
    object.get("u").isUnknown()
    object.get("u").intValue(301) == 301
  }

  def "JsonObject.remove(int) returns the removed member."() {
    when:
    JsonObject object = new JsonObject()
    object.add("x", "!")

    then:
    object.remove(0).value.asString("") == "!"
  }

  def "JsonObject.remove(String) returns the removed member (or null)."() {
    when:
    JsonObject object = new JsonObject()

    then:
    object.remove("missing") == null

    when:
    object.add("x", "!")

    then:
    object.remove("x").value.asString("") == "!"
  }

  def "Cannot remove member from empty object."() {
    when:
    JsonObject object = new JsonObject()
    object.remove(1)

    then:
    thrown IndexOutOfBoundsException
  }

  def "Cannot remove member with negative index."() {
    when:
    JsonObject object = new JsonObject()
    object.remove(-2)

    then:
    thrown IndexOutOfBoundsException
  }

  def "JsonObject.addAll(JsonMember...) can add two members."() {
    when:
    JsonObject object = new JsonObject()
    object.addAll(
        new JsonMember("bart", Json.of(10)),
        new JsonMember("bort", Json.of(20)))

    then:
    object.size() == 2
    object.get("bart").asInt(0) == 10
    object.get("bort").asInt(0) == 20

    when:
    object.addAll(
        new JsonMember("lisa", Json.of(1)),
        new JsonMember("lisa", Json.of(2)))

    then:
    // Test that the members are added in argument order.
    object.size() == 4
    object.get("lisa").asInt(0) == 1
  }

  def "It is okay to pass zero arguments to addAll."() {
    when:
    JsonObject object = new JsonObject()
    object.addAll()

    then:
    object.isEmpty()
  }

  def "Can't pass null to addAll (1)."() {
    when:
    JsonObject object = new JsonObject()
    object.addAll((JsonMember[]) null)

    then:
    thrown NullPointerException
  }

  def "Can't pass null to addAll (2)."() {
    when:
    JsonObject object = new JsonObject()
    object.addAll((JsonMember) null)

    then:
    thrown NullPointerException
  }
}
