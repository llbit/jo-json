package se.llbit.json;

@SuppressWarnings("javadoc")
public interface JsonConstants {
	char BEGIN_OBJECT = '{';
	char END_OBJECT = '}';
	char BEGIN_ARRAY = '[';
	char END_ARRAY = ']';
	char NAME_SEPARATOR = ':';
	char VALUE_SEPARATOR = ',';
	char[] TRUE = "true".toCharArray();
	char[] FALSE = "false".toCharArray();
	char[] NULL = "null".toCharArray();
	char QUOTE_MARK = '"';
	char ESCAPE = '\\';
}
