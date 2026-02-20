package com.gui.jhand.hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandUtilsTest {

	private Hand hand;

	private String handAsString;

	private final String heroName = "GuiRodri2013";

	@BeforeEach
	void setUp() {
		handAsString = HandTemplateLoader.valid();
		hand = Hand.of(handAsString, heroName);
	}

	@Test
	void should_get_hand_id() {
		assertThat(HandUtils.getHandId(handAsString)).isEqualTo(hand.getHandId());
	}

	@Test
	void should_get_hero_cards() {
		assertThat(HandUtils.getHeroCards(handAsString, heroName)).isEqualTo(hand.getHeroCards());
	}

	@Test
	void should_check_vpip() {
		assertThat(HandUtils.isVpip(handAsString, heroName)).isEqualTo(hand.isVpip());
	}

	@Test
	void should_check_pfr() {
		assertThat(HandUtils.isPfr(handAsString, heroName)).isEqualTo(hand.isPfr());
	}

}
