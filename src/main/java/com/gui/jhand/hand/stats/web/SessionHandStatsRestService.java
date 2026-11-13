package com.gui.jhand.hand.stats.web;

import com.gui.jhand.hand.stats.SessionHandStatsQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hand-derived statistics of a session. Lives in {@code hand} (not {@code pokersession})
 * because it reads hands, and {@code pokersession} must not know {@code hand}.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/sessions")
class SessionHandStatsRestService {

	private final SessionHandStatsQuery query;

	@GetMapping("/{sessionId}/hand-stats")
	public ResponseEntity<SessionHandStatsResponse> getStats(@PathVariable String sessionId) {
		return ResponseEntity.ok(SessionHandStatsResponse.of(query.getStats(sessionId)));
	}

}
