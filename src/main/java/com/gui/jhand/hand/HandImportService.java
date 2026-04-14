package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.parser.PokerStarsParser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class HandImportService {

	private final HandResultBuilder builder;

	private final HandResultRepository repository;

	private final PokerStarsParser parser = new PokerStarsParser();

	public void saveAllHandResultsFromFile(String fileContent, String fileName, String heroName) {
		String[] rawHands = fileContent.split("(\\r?\\n){2,}");

		List<HandResult> processedHands = new ArrayList<>();

		for (String rawHand : rawHands) {
			if (rawHand.trim().isEmpty()) {
				continue;
			}

			List<Action> actions = parser.parse(rawHand);
			HandResult result = builder.build(actions, fileName, heroName);

			processedHands.add(result);
		}

		repository.saveAll(processedHands);
	}

}
