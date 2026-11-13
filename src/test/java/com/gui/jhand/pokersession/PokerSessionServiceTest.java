package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class PokerSessionServiceTest extends TestBase {

	@Mock
	private PokerSessionRepository repository;

	private PokerSessionService service;

	@Override
	public void init() {
		service = new PokerSessionService(repository);
	}

	@Test
	void should_save_poker_session() {
		PokerSession session = valid(TournamentPokerSession.class);
		when(repository.save(session)).thenReturn(session);

		assertThat(service.save(session)).isEqualTo(session);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).save(session);
		inOrder.verifyNoMoreInteractions();
	}

}
