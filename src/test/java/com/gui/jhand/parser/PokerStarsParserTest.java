package com.gui.jhand.parser;

import com.gui.jhand.action.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionStreet.*;
import static com.gui.jhand.action.ActionType.*;
import static org.assertj.core.api.Assertions.assertThat;

class PokerStarsParserTest {

	private PokerStarsParser parser;

	private ParserContext context;

	@BeforeEach
	void setUp() {
		parser = new PokerStarsParser();
		context = new ParserContext();
	}

	@Test
	void should_parse_hand_header() {
		String line = "PokerStars Hand #259795694323: Tournament #3976713284, $0.92+$0.08 USD Hold'em No Limit - Level II (15/30) - 2026/02/19 21:29:23 BRT [2026/02/19 19:29:23 ET]";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(HEADER, context.getCurrentStreet(), null, "259795694323", 0, line));
	}

	@Test
	void should_parse_table_info() {
		String line = "Table '3976713284 1' 3-max Seat #3 is the button";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(TABLE_INFO, context.getCurrentStreet(), null, "3976713284 1", 0, line));
	}

	@Test
	void should_parse_seat_info() {
		String line = "Seat 8: GuiRodri2013 (1448 in chips)";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(SEAT_INFO, context.getCurrentStreet(), "GuiRodri2013", "8", 0, line));
	}

	@Test
	void should_parse_post_ante() {
		String line = "GuiRodri2013: posts the ante 10";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(POST_ANTE, context.getCurrentStreet(), "GuiRodri2013", "posts the ante 10", 10.0, line));
	}

	@Test
	void should_parse_post_blind() {
		String line = "GuiRodri2013: posts big blind 80";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(POST_BLIND, context.getCurrentStreet(), "GuiRodri2013", "posts blind 80", 80.0, line));
	}

	@Test
	void should_parse_dealt_to_hero() {
		String line = "Dealt to GuiRodri2013 [3h Ts]";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(DEALT_TO_HERO, context.getCurrentStreet(), "GuiRodri2013", "3h Ts", 0, line));
	}

	@Test
	void should_parse_call() {
		String line = "GuiRodri2013: calls 120";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(ACTION_CALL, context.getCurrentStreet(), "GuiRodri2013", "calls 120", 120.0, line));
	}

	@Test
	void should_parse_call_all_in() {
		String line = "GuiRodri2013: calls 1343 and is all-in";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(ACTION_CALL, context.getCurrentStreet(), "GuiRodri2013", "calls 1343", 1343.0, line));
	}

	@Test
	void should_parse_raise() {
		String line = "GuiRodri2013: raises 20 to 40";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(ACTION_RAISE, context.getCurrentStreet(), "GuiRodri2013", "raises 20 to 40", 40, line));
	}

	@Test
	void should_parse_bet() {
		String line = "GuiRodri2013: bets 20";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(ACTION_BET, context.getCurrentStreet(), "GuiRodri2013", "bets 20", 20, line));
	}

	@Test
	void should_parse_flop() {
		String line = "*** FLOP *** [Kc 6s Qc]";

		assertThat(parser.parse(line)).containsExactly(Action.of(FLOP_CARDS, FLOP, null, "Kc 6s Qc", 0, line));
	}

	@Test
	void should_parse_turn() {
		String line = "*** TURN *** [Kc 6s Qc] [8h]";

		assertThat(parser.parse(line)).containsExactly(Action.of(TURN_CARDS, TURN, null, "8h", 0, line));
	}

	@Test
	void should_parse_river() {
		String line = "*** RIVER *** [Kc 6s Qc 8h] [Js]";

		assertThat(parser.parse(line)).containsExactly(Action.of(RIVER_CARDS, RIVER, null, "Js", 0, line));
	}

	@Test
	void should_parse_cards_shown() {
		String line = "GuiRodri2013: shows [Qh 4d] (a pair of Queens)";

		assertThat(parser.parse(line)).containsExactly(Action.of(CARDS_SHOWN, context.getCurrentStreet(),
				"GuiRodri2013", "shows [Qh 4d] (a pair of Queens)", 0, line));
	}

	@Test
	void should_parse_collected_pot() {
		String line = "GuiRodri2013 collected 108 from pot";

		assertThat(parser.parse(line)).containsExactly(Action.of(COLLECTED_POT, context.getCurrentStreet(),
				"GuiRodri2013", "collected 108 from pot", 108.0, line));
	}

	@Test
	void should_parse_collected_side_pot() {
		String line = "GuiRodri2013 collected 40 from side pot";

		assertThat(parser.parse(line)).containsExactly(Action.of(COLLECTED_POT, context.getCurrentStreet(),
				"GuiRodri2013", "collected 40 from side pot", 40.0, line));
	}

	@Test
	void should_parse_summary_header() {
		String line = "*** SUMMARY ***";

		assertThat(parser.parse(line)).containsExactly(Action.of(SUMMARY_HEADER, SUMMARY, null, null, 0, line));
	}

	@Test
	void should_parse_fold() {
		String line = "GuiRodri2013: folds";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(ACTION_FOLD, context.getCurrentStreet(), "GuiRodri2013", "folds", 0, line));
	}

	@Test
	void should_parse_uncalled_bet_returned() {
		String line = "Uncalled bet (57) returned to GuiRodri2013";

		assertThat(parser.parse(line)).containsExactly(
				Action.of(UNCALLED_BET_RETURNED, context.getCurrentStreet(), "GuiRodri2013", "57", 57.0, line));
	}

	@Test
	void should_parse_total_pot() {
		String line = "Total pot 1689 | Rake 0";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(TOTAL_POT, context.getCurrentStreet(), null, "Total pot 1689", 1689.0, line));
	}

	@Test
	void should_parse_total_pot_with_side_pot() {
		String line = "Total pot 3050 Main pot 2850. Side pot 200. | Rake 0";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(TOTAL_POT, context.getCurrentStreet(), null, "Total pot 3050", 3050.0, line));
	}

	@Test
	void should_parse_board() {
		String line = "Board [Js 4c 4d Qs 7h]";

		assertThat(parser.parse(line))
			.containsExactly(Action.of(BOARD, context.getCurrentStreet(), null, "Js 4c 4d Qs 7h", 0, line));
	}

	@Test
	void should_parse_seat_information() {
		String line = "Seat 7: GuiRodri2013 folded before Flop (didn't bet)";

		assertThat(parser.parse(line)).containsExactly(Action.of(SEAT_HAND_RESULT, context.getCurrentStreet(),
				"GuiRodri2013", "folded before Flop (didn't bet)", 0, line));
	}

}
