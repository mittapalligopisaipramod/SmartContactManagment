package com.main.service;


import java.util.regex.Pattern;

public class EmailValidator {
	private static final String EMAIL_PATTERN =
			"^[A-Za-z0-9+_.-]+@(.+)$";
	
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
	public static boolean isValid(String email) {
		if (email == null) {
			return false;
		}
		return pattern.matcher(email).matches();
	}
}

