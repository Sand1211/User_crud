package com.core.myapp.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PasswordUtil {
	
	private PasswordUtil() {
	}

	private static SecureRandom random = new SecureRandom();
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUM = "0123456789";
	private static final String SPL_CHARS = "@#$%^&+=-_!";
	private static final String COMB_CHARS = ALPHA_CAPS + ALPHA + NUM + SPL_CHARS;
	
	private static final String REGEX = 
				"^(?=.*[\\d])"			//(?=.*[d]) represents a digit must occur at least once.
            + "(?=.*[a-z])"				//(?=.*[a-z]) represents a lower case alphabet must occur at least once.
            + "(?=.*[A-Z])"				//(?=.*[A-Z]) represents an upper case alphabet that must occur at least once				
            + "(?=.+[@#$%^&+=\\-_()!])"		//(?=.+[@#$%^&-+=()] represents a special character that must occur at least once.
            + "(?=\\S+$)"				//(?=\\S+$) white spaces don’t allowed in the entire string.
            + ".{8,256}$";				//.{8, 256} represents at least 8 characters and at most 256 characters.

	private static Pattern pattern = Pattern.compile(REGEX);
	
	public static String generatePasssword(int length) {
		List<Character> result = new ArrayList<>();

		result.add(ALPHA.charAt(random.nextInt(ALPHA.length())));
		result.add(ALPHA_CAPS.charAt(random.nextInt(ALPHA_CAPS.length())));
		result.add(SPL_CHARS.charAt(random.nextInt(SPL_CHARS.length())));
		result.add(NUM.charAt(random.nextInt(NUM.length())));

		for (int i = 4; i < length; i++) {
			result.add(COMB_CHARS.charAt(random.nextInt(COMB_CHARS.length())));
		}

		Collections.shuffle(result);
		return result.stream().map(String::valueOf).collect(Collectors.joining());
	}
	
	public static String generateNumber(int length) {
		List<Character> result = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			result.add(NUM.charAt(random.nextInt(NUM.length())));
		}

		Collections.shuffle(result);
		return result.stream().map(String::valueOf).collect(Collectors.joining());
	}

}
