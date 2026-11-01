package com.gui.jhand.evaluator;

import com.gui.jhand.core.poker.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.gui.jhand.core.poker.CardTemplateLoader.*;
import static com.gui.jhand.core.poker.HandRank.*;
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
							HandEvaluationResult result = HandEvaluator.eval(new Card[] { deck.get(c1), deck.get(c2),
									deck.get(c3), deck.get(c4), deck.get(c5) });
							scoresSeen[result.score()] = true;

							switch (result.handRank()) {
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
		Card[] royalFlush = {as(), ks(), qs(), js(), ts()};
		Card[] fourOfKind = {as(), ah(), ad(), ac(), ks()};
		Card[] fullHouse = {as(), ah(), ad(), ks(), kh()};
		Card[] nutFlush = {as(), ks(), qs(), js(), nineS()};
		Card[] flush = {twoS(), sevenS(), qs(), js(), nineS()};
		Card[] sevenHigh = {sevenS(), fiveH(), fourD(), threeC(), twoS()};

		assertThat(HandEvaluator.eval(royalFlush))
			.isEqualTo(new HandEvaluationResult(1, ROYAL_FLUSH));

		assertThat(HandEvaluator.eval(fourOfKind))
			.isEqualTo(new HandEvaluationResult(11, FOUR_OF_A_KIND));

		assertThat(HandEvaluator.eval(fullHouse))
			.isEqualTo(new HandEvaluationResult(167, FULL_HOUSE));

		assertThat(HandEvaluator.eval(nutFlush))
			.isEqualTo(new HandEvaluationResult(323, FLUSH));

		assertThat(HandEvaluator.eval(flush))
			.isEqualTo(new HandEvaluationResult(1182, FLUSH));

		assertThat(HandEvaluator.eval(sevenHigh))
			.isEqualTo(new HandEvaluationResult(7462, HIGH_CARD));
	}

	@Test
	void should_evaluate_7_card_hands_using_best_5_combination() {
		PocketCards pocket = PocketCards.fromHandHistory("ks qs");
		Board board = Board.fromString("As js Ts 2c 3d");

		assertThat(evalSevenCards(pocket, board))
			.isEqualTo(new HandEvaluationResult(1, ROYAL_FLUSH));
	}

	@Test
	void should_evaluate_7_card_hands_with_card_array_and_list() {
		assertThat(HandEvaluator.eval(new Card[] { as(), ah(), ad(), ks(), kh(), twoS(), threeC() }))
			.isEqualTo(new HandEvaluationResult(167, FULL_HOUSE)); // Aces full of Kings
	}

	@Test
	void should_throw_exception_for_invalid_card_counts() {
		assertThatThrownBy(() -> HandEvaluator.eval(null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> HandEvaluator.eval(new Card[6])).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(null, Board.fromString("As ks qs js Ts")))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> evalSevenCards(PocketCards.fromHandHistory("As ks"), null))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
