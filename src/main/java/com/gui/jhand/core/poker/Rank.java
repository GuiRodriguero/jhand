package com.gui.jhand.core.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Rank {

	TWO("2", 0), THREE("3", 1), FOUR("4", 2), FIVE("5", 3), SIX("6", 4), SEVEN("7", 5), EIGHT("8", 6), NINE("9", 7),
	TEN("T", 8), JACK("J", 9), QUEEN("Q", 10), KING("K", 11), ACE("A", 12);

	private final String symbol;

	private final int value;

	public static Rank fromSymbol(String symbol) {
		return Arrays.stream(values())
			.filter(rank -> rank.symbol.equalsIgnoreCase(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown card symbol: " + symbol));
	}

}
