package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.parser.PokerStarsParser;
import com.gui.jhand.pokersession.PokerSessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class HandImportService {

	private final HandResultBuilder builder;

	private final HandResultRepository repository;

	private final PokerSessionService pokerSessionService;

	private final PokerStarsParser parser = new PokerStarsParser();

	public void saveAllHandResultsFromFile(String fileContent, String fileName, String heroName) {
		List<HandResult> processedHands = new ArrayList<>();

		for (String rawHand : fileContent.split("(\\r?\\n){2,}")) {
			if (rawHand.trim().isEmpty()) {
				continue;
			}

			List<Action> actions = parser.parse(rawHand);
			HandResult result = builder.build(actions, fileName, heroName);

			processedHands.add(result);
		}

		if (!processedHands.isEmpty()) {
			repository.saveAll(processedHands);
			pokerSessionService.create(fileName, ZERO);
		}
	}

}
