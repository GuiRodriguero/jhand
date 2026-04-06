package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.handler.*;
import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.parser.PokerStarsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static com.gui.jhand.core.poker.HandRank.*;
import static com.gui.jhand.hand.HandTemplateLoader.*;
import static com.gui.jhand.hand.Position.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class HandResultBuilderTest {

	private HandResultBuilder builder;

	private List<Action> actions;

	private PokerStarsParser parser;

	@BeforeEach
	void setUp() {
		parser = new PokerStarsParser();

		List<ActionHandler> handlers = List.of(new HeaderHandler(), new RaiseHandler(), new CollectedHandler(),
				new DealtToHeroHandler(), new UncalledBetHandler(), new PostAnteHandler(), new SeatSummaryHandler(),
				new InvestmentHandler(), new TimeHandler(), new HandRankHandler());

		builder = new HandResultBuilder(new ActionHandlerRegistry(handlers));
	}

	@Test
	void should_build_hand_result_with_showdown() {
		String hand = HandTemplateLoader.validWonWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827261990", "My Session", Instant.ofEpochSecond(1771774381), "Qd Jh",
				UTG, ChipsUtils.of(800.0), true, true, ChipsUtils.of(970.0), ChipsUtils.of(2490.0),
				ChipsUtils.of(1520.0), PAIR);

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_showdown_lost() {
		String hand = HandTemplateLoader.validLostWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827220105", "My Session", Instant.ofEpochSecond(1771774150), "2h Ac",
				MP, ChipsUtils.of(600.0), true, true, ChipsUtils.of(2555.0), ZERO, ChipsUtils.of(-2555.0), TWO_PAIR);

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_without_showdown() {
		String hand = HandTemplateLoader.validWonWithoutShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827317520", "My Session", Instant.ofEpochSecond(1771774682), "3d 8c",
				BB, ChipsUtils.of(1200.0), false, false, ChipsUtils.of(1275.0), ChipsUtils.of(2550.0),
				ChipsUtils.of(1275.0), null);

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_side_pot() {
		String hand = HandTemplateLoader.validWithSidePot();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859276702", "My Session", Instant.ofEpochSecond(1771975039), "Ad Ac",
				UTG, ChipsUtils.of(20.0), true, true, ChipsUtils.of(1494.0), ChipsUtils.of(2263.0),
				ChipsUtils.of(769.0), HIGH_CARD);

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_side_pot_and_multiple_winners() {
		String hand = HandTemplateLoader.validWithSidePotAndMultipleWinners();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859212603", "My Session", Instant.ofEpochSecond(1771974739), "Jc Qc",
				SB, ChipsUtils.of(50.0), true, false, ChipsUtils.of(950.0), ChipsUtils.of(2850.0),
				ChipsUtils.of(1900.0), HIGH_CARD);

		assertThat(builder.build(actions, "My Session", "GuiRodri2013")).isEqualTo(expected);
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
