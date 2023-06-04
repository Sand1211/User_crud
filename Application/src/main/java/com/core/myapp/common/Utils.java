package com.core.myapp.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

	private Utils() {
	}

	public static boolean isNull(Object object) {
		return object == null;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}

	public static boolean isEmpty(Integer i) {
		return i == null || i==0;
	}

	public static boolean isEmpty(byte[] byt) {
		return byt == null;
	}

	public static boolean isTrue(Boolean flag) {
		return flag != null && flag;
	}
	
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}
	
	public static boolean isEmpty(Set<?> set) {
		return set == null || set.isEmpty();
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	public static <T> T getFirstElement(List<T> list) {
		if(!isEmpty(list)) {
			T item = list.get(0);
			if(!isNull(item)) {
				return item;
			}
		}
		return null;
	}
	
	public static String firstCharacterToUpperCase(String string) {
		if (Utils.isEmpty(string)) {
			return string;
		}
		char updatedChar = Character.toUpperCase(string.charAt(0));

		char[] chars = string.toCharArray();
		chars[0] = updatedChar;
		return new String(chars);
	}
	
	public static String firstCharacterToLowerCase(String string) {
		if (Utils.isEmpty(string)) {
			return string;
		}
		char updatedChar = Character.toLowerCase(string.charAt(0));

		char[] chars = string.toCharArray();
		chars[0] = updatedChar;
		return new String(chars);
	}
}
