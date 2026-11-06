package com.gui.jhand.equity.web;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;

import static org.springframework.util.StringUtils.hasText;

public record EquityRequest(String heroCards, String villainCards, String board) {

	PocketCards toHeroCards() {
		return toPocketCards("Hero", heroCards);
	}

	PocketCards toVillainCards() {
		return toPocketCards("Villain", villainCards);
	}

	Board toBoard() {
		return hasText(board) ? Board.fromString(board) : Board.of();
	}

	private static PocketCards toPocketCards(String player, String cards) {
		if (!hasText(cards)) {
			throw new IllegalArgumentException(player + " cards are required");
		}
		return PocketCards.fromHandHistory(cards.trim());
	}

}
