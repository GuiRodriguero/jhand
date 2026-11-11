package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class PokerSessionServiceTest extends TestBase {

	private static final String SESSION_ID = "HH20260425 T3995173048 No Limit Hold'em $0.90 + $0.10";

	@Mock
	private PokerSessionRepository repository;

	private PokerSessionService service;

	@Override
	public void init() {
		service = new PokerSessionService(repository);
	}

	@Test
	void should_create_poker_session() {
		BigDecimal netProfit = BigDecimal.valueOf(100.00);
		Instant startTime = Instant.ofEpochSecond(1771774381);
		PokerSession expected = PokerSession.of(SESSION_ID, TOURNAMENT, 42, startTime, netProfit);
		PokerSession session = valid(PokerSession.class);

		when(repository.save(expected)).thenReturn(session);

		assertThat(service.create(SESSION_ID, TOURNAMENT, 42, startTime, netProfit)).isEqualTo(session);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).save(expected);
		inOrder.verifyNoMoreInteractions();
	}

}
