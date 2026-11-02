package com.gui.jhand.core.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum Suit {

	CLUBS("c", 1), DIAMONDS("d", 2), HEARTS("h", 4), SPADES("s", 8);

	private final String symbol;

	private final int value;

	public static Suit fromSymbol(String symbol) {
		return Arrays.stream(values())
			.filter(suit -> suit.symbol.equalsIgnoreCase(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown suit symbol: " + symbol));
	}

}
