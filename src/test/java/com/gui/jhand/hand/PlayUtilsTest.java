package com.gui.jhand.hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class PlayUtilsTest {

	private String handAsString;

	private final String heroName = "GuiRodri2013";

	@BeforeEach
	void setUp() {
		handAsString = HandTemplateLoader.validAsString();
	}

	@Test
	void should_get_plays_on_hand() {
		List<Play> plays = PlayUtils.getPlaysOnHand(handAsString, heroName);

		assertThat(plays).isNotEmpty();

		for (Play play : plays) {
			assertThat(play.getPlayer()).isEqualTo(heroName);
			assertThat(handAsString).contains(heroName + ": " + (play.getAction().name().toLowerCase()));

			if (play.getAmount() > 0) {
				String amountStr = String.valueOf((int) play.getAmount());
				assertThat(handAsString).containsPattern(Pattern.quote(heroName) + ": " + ".*" + amountStr);
			}
		}
	}

}
