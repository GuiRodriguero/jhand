package com.gui.jhand.evaluator;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.poker.Card;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.*;
import static com.gui.jhand.core.poker.Suit.*;
import static com.gui.jhand.evaluator.HandEvaluatorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandEvaluatorUtilsTest extends TestBase {

	@Override
	public void init() {

	}

	@Test
	void should_convert_card_to_correct_32_bit_integer() {
		Card aceOfSpades = new Card(ACE, SPADES);
		int card = convertCard(aceOfSpades);

		int prime = card & 0xFF;
		int rank = (card >> 8) & 0xF;
		int suit = (card >> 12) & 0xF;
		int rankBitmask = card >> 16;

		assertThat(prime).isEqualTo(41);
		assertThat(rank).isEqualTo(ACE.getValue());
		assertThat(suit).isEqualTo(SPADES.getValue());
		assertThat(rankBitmask).isEqualTo(1 << 12);
	}

	@Test
	void should_throw_exception_when_card_is_null() {
		assertThatThrownBy(() -> convertCard(null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_check_if_all_cards_have_the_same_suit() {
		assertThat(allSuitsAreEqual(convertCard(new Card(ACE, SPADES)), convertCard(new Card(KING, SPADES)),
				convertCard(new Card(QUEEN, SPADES)), convertCard(new Card(JACK, SPADES)),
				convertCard(new Card(TEN, SPADES))))
			.isTrue();
	}

	@Test
	void should_check_if_all_cards_have_the_same_suit_false() {
		assertThat(allSuitsAreEqual(convertCard(new Card(ACE, SPADES)), convertCard(new Card(ACE, CLUBS)),
				convertCard(new Card(ACE, DIAMONDS)), convertCard(new Card(ACE, HEARTS)),
				convertCard(new Card(KING, SPADES))))
			.isFalse();
		assertThat(allSuitsAreEqual(convertCard(new Card(ACE, SPADES)), convertCard(new Card(KING, SPADES)),
				convertCard(new Card(QUEEN, SPADES)), convertCard(new Card(JACK, SPADES)),
				convertCard(new Card(TEN, HEARTS))))
			.isFalse();
	}

	@Test
	void should_get_the_prime_representation_of_a_card() {
		assertThat(getPrimeRepresentation(convertCard(new Card(TWO, SPADES)))).isEqualTo(2);
		assertThat(getPrimeRepresentation(convertCard(new Card(THREE, SPADES)))).isEqualTo(3);
		assertThat(getPrimeRepresentation(convertCard(new Card(FOUR, SPADES)))).isEqualTo(5);
		assertThat(getPrimeRepresentation(convertCard(new Card(FIVE, SPADES)))).isEqualTo(7);
		assertThat(getPrimeRepresentation(convertCard(new Card(SIX, SPADES)))).isEqualTo(11);
		assertThat(getPrimeRepresentation(convertCard(new Card(SEVEN, SPADES)))).isEqualTo(13);
		assertThat(getPrimeRepresentation(convertCard(new Card(EIGHT, SPADES)))).isEqualTo(17);
		assertThat(getPrimeRepresentation(convertCard(new Card(NINE, SPADES)))).isEqualTo(19);
		assertThat(getPrimeRepresentation(convertCard(new Card(TEN, SPADES)))).isEqualTo(23);
		assertThat(getPrimeRepresentation(convertCard(new Card(JACK, SPADES)))).isEqualTo(29);
		assertThat(getPrimeRepresentation(convertCard(new Card(QUEEN, SPADES)))).isEqualTo(31);
		assertThat(getPrimeRepresentation(convertCard(new Card(KING, SPADES)))).isEqualTo(37);
		assertThat(getPrimeRepresentation(convertCard(new Card(ACE, SPADES)))).isEqualTo(41);

		Card card = valid(Card.class);
		int primeCard = convertCard(card) & 0xFF;
		assertThat(getPrimeRepresentation(convertCard(card))).isEqualTo(primeCard);
	}

}
