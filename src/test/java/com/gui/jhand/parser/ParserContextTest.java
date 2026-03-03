package com.gui.jhand.parser;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionStreet.FLOP;
import static org.assertj.core.api.Assertions.assertThat;

class ParserContextTest {

	@Test
	void should_change_current_street() {
		ParserContext context = new ParserContext();

		context.changeCurrentStreetTo(FLOP);

		assertThat(context.getCurrentStreet()).isEqualTo(FLOP);
	}

}
