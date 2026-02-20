package com.gui.jhand.hand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class HandTemplateLoader {

	static String valid() {
		String userName = "GuiRodri2013";
		String game = "HH20260219 T3976713284 No Limit Hold'em $0.92 + $0.08.txt";
		Path filePath = Paths.get(Paths.get("src", "test", "resources").toAbsolutePath().toString(), userName, game);

		String content;
		try {
			content = Files.readString(filePath);
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading file", e);
		}

		String[] handsAsString = content.split("(\\R){3,}");

		List<Hand> hands = Arrays.stream(handsAsString)
			.map(String::trim)
			.filter(block -> !block.isEmpty())
			.map(hand -> Hand.of(hand, userName))
			.toList();

		return hands.get((int) (Math.random() * hands.size())).toString();
	}

}
