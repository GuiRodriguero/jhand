package com.gui.jhand.hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.hand.Position.*;
import static org.assertj.core.api.Assertions.assertThat;

class PositionUtilsTest {

	private HandState state;

	@BeforeEach
	void setUp() {
		state = new HandState("hero");
		state.setHeroSummaryLine("GuiRodri2013 folded before Flop (didn't bet)");
	}

	@Test
	void should_resolve_position_explicit() {
		state.setHeroSummaryLine("GuiRodri2013 (button) folded before Flop (didn't bet)");

		assertThat(PositionUtils.resolvePosition(state)).isEqualTo(BTN);
	}

	@Test
	void should_resolve_position_utg() {
		state.setActivePlayersCount(5);
		state.setHeroIndex(4);
		state.setBtnIndex(1);

		assertThat(PositionUtils.resolvePosition(state)).isEqualTo(UTG);
	}

	@Test
	void should_resolve_position_co() {
		state.setActivePlayersCount(5);
		state.setHeroIndex(4);
		state.setBtnIndex(5);

		assertThat(PositionUtils.resolvePosition(state)).isEqualTo(CO);
	}

	@Test
	void should_resolve_position_mp() {
		state.setActivePlayersCount(9);
		state.setHeroIndex(5);
		state.setBtnIndex(1);

		assertThat(PositionUtils.resolvePosition(state)).isEqualTo(MP);
	}

}
