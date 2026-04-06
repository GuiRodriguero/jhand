package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.HandRank.*;
import static org.assertj.core.api.Assertions.assertThat;

class HandRankTest {

	@Test
	void should_get_royal_flush_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Ah Kh] and won (10000) with a Royal Flush";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(ROYAL_FLUSH);
	}

	@Test
	void should_get_straight_flush_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [8c 9h] and won (10000) with a Straight Flush";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(STRAIGHT_FLUSH);
	}

	@Test
	void should_get_four_of_a_kind_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Ac Ah] and won (10000) with a Four of a Kind, Aces";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(FOUR_OF_A_KIND);
	}

	@Test
	void should_get_full_house_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Ac Ah] and won (10000) with a full house, Aces full of Threes";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(FULL_HOUSE);
	}

	@Test
	void should_get_flush_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [2d Jd] and won (10000) with a flush, Jacks high";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(FLUSH);
	}

	@Test
	void should_get_straight_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [7d Jc] and won (10000) with a straight, Seven to Jacks";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(STRAIGHT);
	}

	@Test
	void should_get_three_of_a_kind_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Jd Jc] and won (10000) with a three of a kind, Jacks";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(THREE_OF_A_KIND);
	}

	@Test
	void should_get_two_pairs_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Td Jc] and won (10000) with a two pairs, Jacks an Ten";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(TWO_PAIR);
	}

	@Test
	void should_get_pair_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Jd Jc] and won (10000) with a pair, Jacks";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(PAIR);
	}

	@Test
	void should_get_high_card_rank() {
		String resultLine = "Seat 1: GuiRodri2013 (small blind) showed [Ac Jc] and won (10000) with high card Ace";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(HIGH_CARD);
	}

	@Test
	void should_get_high_card_rank_when_no_rank_is_found() {
		String resultLine = "Seat 1: GuiRodri2013 folded before Flop (didn't bet)";

		assertThat(HandRank.fromRawHand(resultLine)).isEqualTo(HIGH_CARD);
	}

}
