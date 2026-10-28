package com.gui.jhand.evaluator;

import com.gui.jhand.core.poker.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.gui.jhand.core.poker.CardTemplateLoader.*;
import static com.gui.jhand.core.poker.HandRank.*;
import static com.gui.jhand.evaluator.HandEvaluator.evalFiveCards;
import static com.gui.jhand.evaluator.HandEvaluator.evalSevenCards;
import static com.gui.jhand.evaluator.HandEvaluatorUtils.convertCard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandEvaluatorTest {

	@Test
	void should_evaluate_all_5_card_poker_hands_with_exact_frequencies() {
		List<Card> deck = Arrays.stream(Suit.values())
			.flatMap(suit -> Arrays.stream(Rank.values()).map(rank -> new Card(rank, suit)))
			.toList();

		int[] cardInts = new int[52];
		for (int i = 0; i < 52; i++) {
			cardInts[i] = convertCard(deck.get(i));
		}

		int totalHands = 0;
		int royalFlushes = 0;
		int straightFlushes = 0;
		int fourOfAKinds = 0;
		int fullHouses = 0;
		int flushes = 0;
		int straights = 0;
		int threeOfAKinds = 0;
		int twoPairs = 0;
		int onePairs = 0;
		int highCards = 0;

		boolean[] scoresSeen = new boolean[7463];

		for (int c1 = 0; c1 < 48; c1++) {
			for (int c2 = c1 + 1; c2 < 49; c2++) {
				for (int c3 = c2 + 1; c3 < 50; c3++) {
					for (int c4 = c3 + 1; c4 < 51; c4++) {
						for (int c5 = c4 + 1; c5 < 52; c5++) {
							totalHands++;
							int score = evalFiveCards(cardInts[c1], cardInts[c2], cardInts[c3], cardInts[c4],
									cardInts[c5]);
							scoresSeen[score] = true;

							HandRank rank = getHandRank(score);
							switch (rank) {
								case ROYAL_FLUSH -> royalFlushes++;
								case STRAIGHT_FLUSH -> straightFlushes++;
								case FOUR_OF_A_KIND -> fourOfAKinds++;
								case FULL_HOUSE -> fullHouses++;
								case FLUSH -> flushes++;
								case STRAIGHT -> straights++;
								case THREE_OF_A_KIND -> threeOfAKinds++;
								case TWO_PAIR -> twoPairs++;
								case PAIR -> onePairs++;
								case HIGH_CARD -> highCards++;
							}
						}
					}
				}
			}
		}

		assertThat(totalHands).isEqualTo(2_598_960);
		assertThat(royalFlushes).isEqualTo(4);
		assertThat(straightFlushes).isEqualTo(36);
		assertThat(fourOfAKinds).isEqualTo(624);
		assertThat(fullHouses).isEqualTo(3_744);
		assertThat(flushes).isEqualTo(5_108);
		assertThat(straights).isEqualTo(10_200);
		assertThat(threeOfAKinds).isEqualTo(54_912);
		assertThat(twoPairs).isEqualTo(123_552);
		assertThat(onePairs).isEqualTo(1_098_240);
		assertThat(highCards).isEqualTo(1_302_540);

		int distinctScores = 0;
		for (int i = 1; i <= 7462; i++) {
			if (scoresSeen[i]) {
				distinctScores++;
			}
		}
		assertThat(distinctScores).isEqualTo(7462);
		assertThat(scoresSeen[0]).isFalse();
	}

	@Test
	void should_evaluate_specific_5_card_hands_correctly() {
		// Royal Flush
		int royalScore = evalFiveCards(as(), ks(), qs(), js(), ts());
		assertThat(royalScore).isEqualTo(1);
		assertThat(getHandRank(royalScore)).isEqualTo(ROYAL_FLUSH);

		// Four of a kind (Aces with King kicker)
		int quadsScore = evalFiveCards(as(), ah(), ad(), ac(), ks());
		assertThat(quadsScore).isEqualTo(11);
		assertThat(getHandRank(quadsScore)).isEqualTo(FOUR_OF_A_KIND);

		// Full House (Aces full of Kings)
		int fullHouseScore = evalFiveCards(as(), ah(), ad(), ks(), kh());
		assertThat(fullHouseScore).isEqualTo(167);
		assertThat(getHandRank(fullHouseScore)).isEqualTo(FULL_HOUSE);

		// Flush (Nut flush)
		int nutFlushScore = evalFiveCards(as(), ks(), qs(), js(), nineS());
		assertThat(nutFlushScore).isEqualTo(323);
		assertThat(getHandRank(nutFlushScore)).isEqualTo(FLUSH);

		// Flush
		int flushScore = evalFiveCards(twoS(), sevenS(), qs(), js(), nineS());
		assertThat(flushScore).isEqualTo(1182);
		assertThat(getHandRank(flushScore)).isEqualTo(FLUSH);

		// High card (Worst high card: 7-5-4-3-2 unsuited)
		int worstHighCardScore = evalFiveCards(sevenS(), fiveH(), fourD(), threeC(), twoS());
		assertThat(worstHighCardScore).isEqualTo(7462);
		assertThat(getHandRank(worstHighCardScore)).isEqualTo(HIGH_CARD);

		assertThat(royalScore).isLessThan(quadsScore);
		assertThat(quadsScore).isLessThan(fullHouseScore);
		assertThat(fullHouseScore).isLessThan(nutFlushScore);
		assertThat(nutFlushScore).isLessThan(flushScore);
		assertThat(flushScore).isLessThan(worstHighCardScore);
	}

	@Test
	void should_evaluate_7_card_hands_using_best_5_combination() {
		PocketCards pocket = PocketCards.fromHandHistory("ks qs");
		Board board = Board.fromString("As js Ts 2c 3d");

		int score = evalSevenCards(pocket, board);
		assertThat(score).isEqualTo(1);
		assertThat(getHandRank(score)).isEqualTo(ROYAL_FLUSH);
	}

	@Test
	void should_evaluate_7_card_hands_with_card_array_and_list() {
		int arrayScore = evalSevenCards(new Card[] { as(), ah(), ad(), ks(), kh(), twoS(), threeC() });

		assertThat(arrayScore).isEqualTo(167); // Aces full of Kings
		assertThat(getHandRank(arrayScore)).isEqualTo(FULL_HOUSE);
	}

	@Test
	void should_throw_exception_for_invalid_card_counts() {
		assertThatThrownBy(() -> evalSevenCards((int[]) null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(new int[] { 1, 2, 3 })).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards((Card[]) null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(new Card[6])).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(null, Board.fromString("As ks qs js Ts")))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(PocketCards.fromHandHistory("As ks"), null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	public static HandRank getHandRank(int score) {
		if (score > 6185) {
			return HIGH_CARD;
		}
		if (score > 3325) {
			return PAIR;
		}
		if (score > 2467) {
			return TWO_PAIR;
		}
		if (score > 1609) {
			return THREE_OF_A_KIND;
		}
		if (score > 1599) {
			return STRAIGHT;
		}
		if (score > 322) {
			return FLUSH;
		}
		if (score > 166) {
			return FULL_HOUSE;
		}
		if (score > 10) {
			return FOUR_OF_A_KIND;
		}
		if (score > 1) {
			return STRAIGHT_FLUSH;
		}
		return ROYAL_FLUSH;
	}

}
