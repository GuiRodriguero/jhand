package com.gui.jhand.hand;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.hand.HandTemplateLoader.valid;
import static org.assertj.core.api.Assertions.assertThat;

class HandTest {

	@Test
	void should_instantiate() {
		String hand = valid();
		String userName = "GuiRodri2013";

		assertThat(Hand.of(hand, userName))
			.isEqualTo(Hand.of(HandUtils.getHandId(hand), HandUtils.getHeroCards(hand, userName),
					HandUtils.isVpip(hand, userName), HandUtils.isPfr(hand, userName)));
	}

}
