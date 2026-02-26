package com.gui.jhand.hand;

import com.gui.jhand.parser.PokerStarsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.hand.HandTemplateLoader.*;
import static com.gui.jhand.hand.Position.*;
import static org.assertj.core.api.Assertions.assertThat;

class HandUtilsTest {

	private Hand hand;

	private String handAsString;

	private final String heroName = "GuiRodri2013";

	private final PokerStarsParser parser = new PokerStarsParser();

	@BeforeEach
	void setUp() {
		handAsString = HandTemplateLoader.validAsString();
		hand = Hand.of(handAsString, heroName);
	}

	@Test
	void should_get_hand_id() {
		assertThat(HandUtils.getHandId(handAsString)).isEqualTo(hand.getHandId());
		assertThat(parser.parse(handAsString, heroName).getHandId()).isEqualTo(hand.getHandId());
	}

	@Test
	void should_get_hero_cards() {
		assertThat(HandUtils.getHeroCards(handAsString, heroName)).isEqualTo(hand.getHeroCards());
		assertThat(parser.parse(handAsString, heroName).getHeroCards()).isEqualTo(hand.getHeroCards());
	}

	@Test
	void should_check_vpip() {
		assertThat(HandUtils.isVpip(handAsString, heroName)).isEqualTo(hand.isVpip());
		assertThat(parser.parse(handAsString, heroName).isVpip()).isEqualTo(hand.isVpip());
	}

	@Test
	void should_check_pfr() {
		assertThat(HandUtils.isPfr(handAsString, heroName)).isEqualTo(hand.isPfr());
		assertThat(parser.parse(handAsString, heroName).isPfr()).isEqualTo(hand.isPfr());
	}

	@Test
	void should_get_position_btn() {
		assertThat(HandUtils.getPosition(heroAtBtn(), heroName)).isEqualTo(BTN);
	}

	@Test
	void should_get_position_sb() {
		assertThat(HandUtils.getPosition(heroAtSB(), heroName)).isEqualTo(SB);
	}

	@Test
	void should_get_position_bb() {
		assertThat(HandUtils.getPosition(heroAtBB(), heroName)).isEqualTo(BB);
	}

	@Test
	void should_get_position_mp() {
		assertThat(HandUtils.getPosition(heroAtMP(), heroName)).isEqualTo(MP);
		assertThat(HandUtils.getPosition(heroAtMPWith8Players(), heroName)).isEqualTo(MP);
	}

	@Test
	void should_get_position_co() {
		assertThat(HandUtils.getPosition(heroAtCO(), heroName)).isEqualTo(CO);
	}

	@Test
	void should_get_position_utg() {
		assertThat(HandUtils.getPosition(heroAtUTG(), heroName)).isEqualTo(UTG);
	}

	@Test
	void should_get_net_profit() {
		assertThat(HandUtils.getNetProfit(validWonWithShowdown(), heroName)).isEqualTo(1520);
		assertThat(parser.parse(validWonWithShowdown(), heroName).getNetProfit()).isEqualTo(1520);
		assertThat(HandUtils.getNetProfit(validWonWithoutShowdown(), heroName)).isEqualTo(1275);
		assertThat(parser.parse(validWonWithoutShowdown(), heroName).getNetProfit()).isEqualTo(1275);
		assertThat(HandUtils.getNetProfit(validWithSidePot(), heroName)).isEqualTo(769);
		assertThat(parser.parse(validWithSidePot(), heroName).getNetProfit()).isEqualTo(769);
		assertThat(HandUtils.getNetProfit(validWithSidePotAndMultipleWinners(), heroName)).isEqualTo(1900);
		assertThat(parser.parse(validWithSidePotAndMultipleWinners(), heroName).getNetProfit()).isEqualTo(1900);
	}

}
