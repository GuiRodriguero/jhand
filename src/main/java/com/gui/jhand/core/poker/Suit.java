package com.gui.jhand.core.poker;

import lombok.AllArgsConstructor;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
enum Suit {

	CLUBS("c"), DIAMONDS("d"), HEARTS("h"), SPADES("s");

	private final String symbol;

	public static Suit fromSymbol(String symbol) {
		return Arrays.stream(values())
			.filter(suit -> suit.symbol.equalsIgnoreCase(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown suit symbol: " + symbol));
	}

}
