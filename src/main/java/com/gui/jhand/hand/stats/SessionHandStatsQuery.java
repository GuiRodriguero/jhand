package com.gui.jhand.hand.stats;

import com.gui.jhand.core.exception.ResourceNotFoundException;
import com.gui.jhand.hand.HandResult;
import com.gui.jhand.hand.HandResultQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SessionHandStatsQuery {

	private final HandResultQuery handResultQuery;

	public SessionHandStats getStats(String sessionId) {
		List<HandResult> hands = handResultQuery.findAllBySessionId(sessionId);

		if (hands.isEmpty()) {
			throw new ResourceNotFoundException("Session %s not found".formatted(sessionId));
		}

		return SessionHandStats.of(hands.stream().map(SessionHand::of).toList());
	}

}
