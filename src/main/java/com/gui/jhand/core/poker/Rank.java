package com.gui.jhand.core.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Rank {

	TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
	TEN("T", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13), ACE("A", 14);

	private final String symbol;

	private final int value;

	public static Rank fromSymbol(String symbol) {
		return Arrays.stream(values())
			.filter(rank -> rank.symbol.equalsIgnoreCase(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown card symbol: " + symbol));
	}

}
