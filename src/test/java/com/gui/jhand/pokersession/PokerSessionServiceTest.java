package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.math.BigDecimal;

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
		PokerSession session = valid(PokerSession.class);

		when(repository.save(PokerSession.of(SESSION_ID, netProfit))).thenReturn(session);

		assertThat(service.create(SESSION_ID, netProfit)).isEqualTo(session);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).save(PokerSession.of(SESSION_ID, netProfit));
		inOrder.verifyNoMoreInteractions();
	}

}
