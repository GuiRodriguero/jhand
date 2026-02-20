package com.gui.jhand.extractor;

import com.gui.jhand.hand.Hand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HandFileExtractorTest {

	HandFileExtractor extractor = new HandFileExtractor();

	private final String testResourcesPath = Paths.get("src", "test", "resources").toAbsolutePath().toString();

	private final String userName = "GuiRodri2013";

	private final String game = "HH20260219 T3976713284 No Limit Hold'em $0.92 + $0.08.txt";

	@Test
	void should_get_all_hands() throws IOException {
		Path filePath = Paths.get(testResourcesPath, userName, game);
		String content = Files.readString(filePath);
		String[] handsAsString = content.split("(\\R){3,}");

		List<Hand> hands = Arrays.stream(handsAsString)
			.map(String::trim)
			.filter(block -> !block.isEmpty())
			.map(hand -> Hand.of(hand, userName))
			.toList();

		assertThat(extractor.getAllHands(testResourcesPath, userName, game)).containsAll(hands);
	}

	@Test
	void should_get_number_of_hands() {
		assertThat(extractor.getNumberOfHands(testResourcesPath, userName, game))
			.isEqualTo(extractor.getAllHands(testResourcesPath, userName, game).size());
	}

}
