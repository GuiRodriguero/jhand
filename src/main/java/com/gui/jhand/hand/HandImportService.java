package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.parser.PokerStarsParser;
import com.gui.jhand.pokersession.PokerSessionService;
import com.gui.jhand.pokersession.SessionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.gui.jhand.pokersession.PokerSessionUtils.normalizeSessionId;
import static com.gui.jhand.pokersession.SessionType.fromFileName;
import static java.math.BigDecimal.ZERO;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HandImportService {

	private final HandResultBuilder builder;

	private final HandResultRepository repository;

	private final PokerSessionService pokerSessionService;

	private final PokerStarsParser parser = new PokerStarsParser();

	public void saveAllHandResultsFromFile(String fileContent, String fileName, String heroName) {
		String sessionId = normalizeSessionId(fileName);
		SessionType sessionType = fromFileName(sessionId);
		List<HandResult> processedHands = new ArrayList<>();

		for (String rawHand : fileContent.split("(\\r?\\n){2,}")) {
			if (rawHand.trim().isEmpty()) {
				continue;
			}

			List<Action> actions = parser.parse(rawHand);
			HandResult result = builder.build(actions, sessionId, sessionType, heroName);

			processedHands.add(result);
		}

		if (!processedHands.isEmpty()) {
			repository.saveAll(processedHands);
			pokerSessionService.create(sessionId, sessionType, processedHands.size(), firstHandTime(processedHands),
					sumNetProfit(processedHands));
		}
	}

	private static BigDecimal sumNetProfit(List<HandResult> hands) {
		return hands.stream().map(HandResult::getNetProfit).reduce(ZERO, BigDecimal::add);
	}

	private static Instant firstHandTime(List<HandResult> hands) {
		return hands.stream().map(HandResult::getTime).filter(Objects::nonNull).min(Instant::compareTo).orElse(null);
	}

}
