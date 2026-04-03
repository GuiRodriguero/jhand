package com.gui.jhand.core.poker;

public enum HandRank {

	HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH,
	ROYAL_FLUSH;

	public static HandRank fromRawHand(String resultLine) {
		String lowerLine = resultLine.toLowerCase();

		if (lowerLine.contains("royal flush"))
			return ROYAL_FLUSH;
		if (lowerLine.contains("straight flush"))
			return STRAIGHT_FLUSH;
		if (lowerLine.contains("four of a kind"))
			return FOUR_OF_A_KIND;
		if (lowerLine.contains("full house"))
			return FULL_HOUSE;
		if (lowerLine.contains("flush"))
			return FLUSH;
		if (lowerLine.contains("straight"))
			return STRAIGHT;
		if (lowerLine.contains("three of a kind"))
			return THREE_OF_A_KIND;
		if (lowerLine.contains("two pair"))
			return TWO_PAIR;
		if (lowerLine.contains("pair"))
			return PAIR;

		return HIGH_CARD;
	}

}
