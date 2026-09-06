package com.gui.jhand.core.poker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record PocketCards(List<Card> cards) {

	public PocketCards {
		cards = cards.stream()
			.sorted(Comparator.comparing(Card::rank, Comparator.comparingInt(Rank::getValue).reversed()))
			.toList();
	}

	public static PocketCards fromHandHistory(String cards) {
		return new PocketCards(Arrays.stream(cards.split(" ")).map(Card::fromHandHistory).toList());
	}

	public static PocketCards fromDb(String cards) {
		return fromHandHistory(cards);
	}

	@Override
	public String toString() {
		return cards.stream().map(Card::toString).collect(Collectors.joining(" "));
	}

}