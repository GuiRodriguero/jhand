package com.gui.jhand.hand.stats;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.exception.ResourceNotFoundException;
import com.gui.jhand.hand.HandResult;
import com.gui.jhand.hand.HandResultQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class SessionHandStatsQueryTest extends TestBase {

	@Mock
	private HandResultQuery handResultQuery;

	private SessionHandStatsQuery query;

	@Override
	public void init() {
		query = new SessionHandStatsQuery(handResultQuery);
	}

	@Test
	void should_get_stats() {
		HandResult hand = valid(HandResult.class);
		when(handResultQuery.findAllBySessionId("My Session")).thenReturn(List.of(hand));

		assertThat(query.getStats("My Session")).isEqualTo(SessionHandStats.of(List.of(SessionHand.of(hand))));
	}

	@Test
	void should_throw_resource_not_found_exception_when_session_has_no_hands() {
		when(handResultQuery.findAllBySessionId("Unknown")).thenReturn(List.of());

		assertThatThrownBy(() -> query.getStats("Unknown")).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Session Unknown not found");
	}

}
