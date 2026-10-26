package com.gui.jhand.pokersession;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@AllArgsConstructor
enum SessionType {

	CASH_GAME(Pattern.compile(".*\\$[0-9]+(?:\\.[0-9]+)?\\s*-\\s*\\$[0-9]+(?:\\.[0-9]+)?.*")),
	TOURNAMENT(Pattern.compile(".*\\s+T\\d+.*"));

	private final Pattern pattern;

	public static SessionType fromFileName(String fileName) {
		return Arrays.stream(values())
			.filter(type -> type.pattern.matcher(fileName).matches())
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid or empty file name: " + fileName));
	}

}
