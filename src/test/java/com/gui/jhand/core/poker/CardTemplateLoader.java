package com.gui.jhand.core.poker;

import static com.gui.jhand.core.poker.Rank.*;
import static com.gui.jhand.core.poker.Suit.*;

public class CardTemplateLoader {

	public static Card as() {
		return new Card(ACE, SPADES);
	}

	public static Card ah() {
		return new Card(ACE, HEARTS);
	}

	public static Card ad() {
		return new Card(ACE, DIAMONDS);
	}

	public static Card ac() {
		return new Card(ACE, CLUBS);
	}

	public static Card ks() {
		return new Card(KING, SPADES);
	}

	public static Card kh() {
		return new Card(KING, HEARTS);
	}

	public static Card qs() {
		return new Card(QUEEN, SPADES);
	}

	public static Card js() {
		return new Card(JACK, SPADES);
	}

	public static Card ts() {
		return new Card(TEN, SPADES);
	}

	public static Card nineS() {
		return new Card(NINE, SPADES);
	}

	public static Card sevenS() {
		return new Card(SEVEN, SPADES);
	}

	public static Card fiveH() {
		return new Card(FIVE, HEARTS);
	}

	public static Card fourD() {
		return new Card(FOUR, DIAMONDS);
	}

	public static Card threeC() {
		return new Card(THREE, CLUBS);
	}

	public static Card twoS() {
		return new Card(TWO, SPADES);
	}

}
