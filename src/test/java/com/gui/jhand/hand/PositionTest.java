package com.gui.jhand.hand;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.hand.Position.*;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

class PositionTest {

	@Test
	void should_get_explicit_position_btn() {
		assertThat(Position.getExplicitPosition("GuiRodri2013 (button) folded before Flop (didn't bet)"))
			.isEqualTo(of(BTN));
	}

	@Test
	void should_get_explicit_position_sb() {
		assertThat(Position.getExplicitPosition("GuiRodri2013 (small blind) folded on the Flop")).isEqualTo(of(SB));
	}

	@Test
	void should_get_explicit_position_bb() {
		assertThat(Position.getExplicitPosition("GuiRodri2013 (big blind) folded on the Flop")).isEqualTo(of(BB));
	}

	@Test
	void should_get_explicit_position_null() {
		assertThat(Position
			.getExplicitPosition("GuiRodri2013 showed [4c As] and won (1292) with two pair, Aces and Fours")).isEmpty();
	}

}
