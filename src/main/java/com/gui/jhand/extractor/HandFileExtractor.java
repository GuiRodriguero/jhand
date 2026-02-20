package com.gui.jhand.extractor;

import com.gui.jhand.hand.Hand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class HandFileExtractor {

	List<Hand> getAllHands(String handHistoryFolder, String userName, String game) {
		Path filePath = Paths.get(handHistoryFolder, userName, game);

		if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
			throw new IllegalArgumentException("File not found: " + filePath);
		}

		try {
			String content = Files.readString(filePath);

			String[] handsAsString = content.split("(\\R){3,}");

			return Arrays.stream(handsAsString)
				.map(String::trim)
				.filter(block -> !block.isEmpty())
				.map(hand -> Hand.of(hand, userName))
				.toList();
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading file", e);
		}
	}

	int getNumberOfHands(String handHistoryFolder, String userName, String game) {
		return getAllHands(handHistoryFolder, userName, game).size();
	}

}
