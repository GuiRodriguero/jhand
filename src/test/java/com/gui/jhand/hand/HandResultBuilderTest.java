package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.handler.*;
import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.parser.PokerStarsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.gui.jhand.action.ActionStreet.SUMMARY;
import static com.gui.jhand.action.ActionType.HAND_RANK;
import static com.gui.jhand.core.poker.HandRank.*;
import static com.gui.jhand.core.poker.PocketCards.fromHandHistory;
import static com.gui.jhand.hand.HandTemplateLoader.*;
import static com.gui.jhand.hand.Position.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(OutputCaptureExtension.class)
class HandResultBuilderTest {

	private HandResultBuilder builder;

	private List<Action> actions;

	private PokerStarsParser parser;

	@BeforeEach
	void setUp() {
		parser = new PokerStarsParser();

		List<ActionHandler> handlers = List.of(new HeaderHandler(), new RaiseHandler(), new CollectedHandler(),
				new DealtToHeroHandler(), new UncalledBetHandler(), new PostAnteHandler(), new SeatSummaryHandler(),
				new InvestmentHandler(), new TimeHandler(), new BoardHandler(), new BigBlindLevelHandler(),
				new TotalPotHandler());

		builder = new HandResultBuilder(new ActionHandlerRegistry(handlers), new HandRankResolver());
	}

	@Test
	void should_ignore_text_descriptions_when_calculating_rank(CapturedOutput output) {
		List<Action> handActions = new ArrayList<>(parser.parse(validWonWithShowdown()));
		handActions.add(Action.of(HAND_RANK, SUMMARY, "GuiRodri2013", "a flush", ZERO, "private raw text"));
		handActions.add(Action.of(HAND_RANK, SUMMARY, "opponent", "a straight", ZERO, "opponent raw text"));
		handActions.add(Action.of(HAND_RANK, SUMMARY, "GuiRodri2013", "unknown description", ZERO, "unknown raw text"));
		HandResult result = builder.build(handActions, "private file", "GuiRodri2013");

		assertThat(result.getHandRank()).isEqualTo(PAIR);
		assertThat(output.getOut()).doesNotContain("Hand rank mismatch", "GuiRodri2013", "opponent", "private");
	}

	@Test
	void should_not_compare_or_fallback_when_rank_cannot_be_calculated() {
		List<Action> handActions = new ArrayList<>(
				parser.parse(validWonWithShowdown().replace("Board [Jc 6c 2c 8s As]", "Board [Jc 6c Xx 8s As]")));
		handActions.add(Action.of(HAND_RANK, SUMMARY, "GuiRodri2013", "a flush", ZERO, "raw"));

		assertThatThrownBy(() -> builder.build(handActions, "file", "GuiRodri2013"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Unknown card symbol: X");
	}

	@Test
	void should_build_hand_result_with_showdown() {
		String hand = HandTemplateLoader.validWonWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827261990", "My Session", Instant.ofEpochSecond(1771774381),
				fromHandHistory("Qd Jh"), UTG, ChipsUtils.of(800.0), true, true, ChipsUtils.of(970.0),
				ChipsUtils.of(2490.0), ChipsUtils.of(1520.0), ChipsUtils.of(1.90), ChipsUtils.of(2490.0), PAIR,
				Board.of(Card.fromHandHistory("Jc"), Card.fromHandHistory("6c"), Card.fromHandHistory("2c"),
						Card.fromHandHistory("8s"), Card.fromHandHistory("As")));

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_showdown_lost() {
		String hand = HandTemplateLoader.validLostWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827220105", "My Session", Instant.ofEpochSecond(1771774150),
				fromHandHistory("2h Ac"), MP, ChipsUtils.of(600.0), true, true, ChipsUtils.of(2555.0), ZERO,
				ChipsUtils.of(-2555.0), ChipsUtils.of(-4.26), ChipsUtils.of(6210.0), PAIR,
				Board.of(Card.fromHandHistory("Qs"), Card.fromHandHistory("Jd"), Card.fromHandHistory("9c"),
						Card.fromHandHistory("6h"), Card.fromHandHistory("9h")));

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_without_showdown() {
		String hand = HandTemplateLoader.validWonWithoutShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827317520", "My Session", Instant.ofEpochSecond(1771774682),
				fromHandHistory("3d 8c"), BB, ChipsUtils.of(1200.0), false, false, ChipsUtils.of(1275.0),
				ChipsUtils.of(2550.0), ChipsUtils.of(1275.0), ChipsUtils.of(1.06), ChipsUtils.of(2550.0),
				THREE_OF_A_KIND,
				Board.of(Card.fromHandHistory("8d"), Card.fromHandHistory("Qd"), Card.fromHandHistory("8h")));

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_assign_hand_rank_without_board_cards() {
		String hand = validWonWithShowdown().replace("Board [Jc 6c 2c 8s As]", "");
		HandResult result = builder.build(parser.parse(hand), "My Session", "GuiRodri2013");
		assertThat(result.getHandRank()).isEqualTo(HIGH_CARD);
		assertThat(result.getBoard()).isNull();
	}

	@Test
	void should_throw_illegal_argument_exception_when_evaluating_a_card_that_does_not_exist() {
		String hand = validWonWithShowdown().replace("Board [Jc 6c 2c 8s As]", "Board [Jc 6c Xx 8s As]");
		assertThatThrownBy(() -> builder.build(parser.parse(hand), "My Session", "GuiRodri2013"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Unknown card symbol: X");
	}

	@Test
	void should_build_hand_result_with_side_pot() {
		String hand = HandTemplateLoader.validWithSidePot();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859276702", "My Session", Instant.ofEpochSecond(1771975039),
				fromHandHistory("Ad Ac"), UTG, ChipsUtils.of(20.0), true, true, ChipsUtils.of(1494.0),
				ChipsUtils.of(2263.0), ChipsUtils.of(769.0), ChipsUtils.of(38.45), ChipsUtils.of(4487.0), PAIR,
				Board.of(Card.fromHandHistory("Td"), Card.fromHandHistory("7d"), Card.fromHandHistory("9d"),
						Card.fromHandHistory("2c"), Card.fromHandHistory("3h")));

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_side_pot_and_multiple_winners() {
		String hand = HandTemplateLoader.validWithSidePotAndMultipleWinners();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859212603", "My Session", Instant.ofEpochSecond(1771974739),
				fromHandHistory("Jc Qc"), SB, ChipsUtils.of(50.0), true, false, ChipsUtils.of(950.0),
				ChipsUtils.of(2850.0), ChipsUtils.of(1900.0), ChipsUtils.of(38.0), ChipsUtils.of(3050.0), STRAIGHT,
				Board.of(Card.fromHandHistory("3d"), Card.fromHandHistory("2c"), Card.fromHandHistory("As"),
						Card.fromHandHistory("Kh"), Card.fromHandHistory("Th")));

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_use_level_big_blind_when_big_blind_is_posted_short_all_in() {
		actions = parser.parse(heroWithShortBigBlind());

		HandResult result = builder.build(actions, "My Session", "GuiRodri2013");

		assertThat(result.getBlindValue()).isEqualTo(ChipsUtils.of(60.0));
		assertThat(result.getTotalInvested()).isEqualTo(ChipsUtils.of(25.0));
		assertThat(result.getTotalCollected()).isEqualTo(ChipsUtils.of(50.0));
		assertThat(result.getNetProfit()).isEqualTo(ChipsUtils.of(25.0));
		assertThat(result.getNetProfitInBb()).isEqualTo(ChipsUtils.of(0.42));
		assertThat(result.getTotalPot()).isEqualTo(ChipsUtils.of(50.0));
	}

	@Test
	void should_build_with_btn_position() {
		actions = parser.parse(heroAtBtn());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(BTN);
	}

	@Test
	void should_build_with_sb_position() {
		actions = parser.parse(heroAtSB());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(SB);
	}

	@Test
	void should_build_with_bb_position() {
		actions = parser.parse(heroAtBB());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(BB);
	}

	@Test
	void should_build_with_utg_position() {
		actions = parser.parse(heroAtUTG());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(UTG);
	}

	@Test
	void should_build_with_mp_position() {
		actions = parser.parse(heroAtMP());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(MP);
	}

	@Test
	void should_build_with_co_position() {
		actions = parser.parse(heroAtCO());

		assertThat(builder.build(actions, "My Session", "GuiRodri2013").getPosition()).isEqualTo(CO);
	}

}
