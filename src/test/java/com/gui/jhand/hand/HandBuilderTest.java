package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.handler.*;
import com.gui.jhand.parser.PokerStarsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.hand.HandTemplateLoader.*;
import static com.gui.jhand.hand.Position.*;
import static org.assertj.core.api.Assertions.assertThat;

class HandBuilderTest {

	private HandBuilder builder;

	private List<Action> actions;

	private PokerStarsParser parser;

	@BeforeEach
	void setUp() {
		parser = new PokerStarsParser();

		List<ActionHandler> handlers = List.of(new HeaderHandler(), new RaiseHandler(), new CollectedHandler(),
				new DealtToHeroHandler(), new UncalledBetHandler(), new PostAnteHandler(), new SeatSummaryHandler(),
				new InvestmentHandler());

		builder = new HandBuilder(handlers);
	}

	@Test
	void should_build_hand_result_with_showdown() {
		String hand = HandTemplateLoader.validWonWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827261990", "Qd Jh", UTG, true, true, 970.0, 2490.0, 1520.0);

		assertThat(builder.build(actions, "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_showdown_lost() {
		String hand = HandTemplateLoader.validLostWithShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827220105", "2h Ac", MP, true, true, 2555.0, 0.0, -2555.0);

		assertThat(builder.build(actions, "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_without_showdown() {
		String hand = HandTemplateLoader.validWonWithoutShowdown();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259827317520", "3d 8c", BB, false, false, 1275.0, 2550.0, 1275.0);

		assertThat(builder.build(actions, "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_side_pot() {
		String hand = HandTemplateLoader.validWithSidePot();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859276702", "Ad Ac", UTG, true, true, 1494.0, 2263.0, 769.0);

		assertThat(builder.build(actions, "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_hand_result_with_side_pot_and_multiple_winners() {
		String hand = HandTemplateLoader.validWithSidePotAndMultipleWinners();
		actions = parser.parse(hand);

		HandResult expected = HandResult.of("259859212603", "Jc Qc", SB, true, false, 950.0, 2850.0, 1900.0);

		assertThat(builder.build(actions, "GuiRodri2013")).isEqualTo(expected);
	}

	@Test
	void should_build_with_btn_position() {
		actions = parser.parse(heroAtBtn());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(BTN);
	}

	@Test
	void should_build_with_sb_position() {
		actions = parser.parse(heroAtSB());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(SB);
	}

	@Test
	void should_build_with_bb_position() {
		actions = parser.parse(heroAtBB());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(BB);
	}

	@Test
	void should_build_with_utg_position() {
		actions = parser.parse(heroAtUTG());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(UTG);
	}

	@Test
	void should_build_with_mp_position() {
		actions = parser.parse(heroAtMP());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(MP);
	}

	@Test
	void should_build_with_co_position() {
		actions = parser.parse(heroAtCO());

		assertThat(builder.build(actions, "GuiRodri2013").getPosition()).isEqualTo(CO);
	}

}
