package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import com.gui.jhand.parser.PokerStarsParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HandImportServiceTest extends TestBase {

	@Mock
	private HandResultBuilder builder;

	@Mock
	private HandResultRepository repository;

	private HandImportService service;

	private PokerStarsParser parser;

	private String rawHands;

	@Override
	public void init() {
		service = new HandImportService(builder, repository);
		parser = new PokerStarsParser();
		rawHands = HandTemplateLoader.heroAtBtn().concat("\n\n\n").concat(HandTemplateLoader.heroAtSB());
	}

	@Test
	void should_save_all_hand_results_from_file() {
		when(builder.build(parser.parse(anyString()), anyString())).thenReturn(valid(HandResult.class));

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, "GuiRodri2013"));
	}

}
