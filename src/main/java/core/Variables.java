package core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Variables {

	private Variables(){

	}

	private static Map<String, String> variables = new HashMap<>();


	public static void put(String key, String value){
		variables.put(key, value);
	}

	public static String get(String key){
		return variables.get(key);
	}

	public static boolean containsKey(String key) {
		return variables.containsKey(key);
	}

	public static String remove(String key) {
		return variables.remove(key);
	}

	public static Set<Map.Entry<String, String>> entrySet() {
		return variables.entrySet();
	}
}
