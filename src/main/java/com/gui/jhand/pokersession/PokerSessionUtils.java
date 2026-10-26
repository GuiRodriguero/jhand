package com.gui.jhand.pokersession;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PokerSessionUtils {

	public static String normalizeSessionId(String sessionId) {
		return sessionId.replaceFirst("\\.txt$", "");
	}

}
