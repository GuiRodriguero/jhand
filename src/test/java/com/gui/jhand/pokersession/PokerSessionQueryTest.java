package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class PokerSessionQueryTest extends TestBase {

	@Mock
	private PokerSessionRepository repository;

	private PokerSessionQuery query;

	private Pageable pageable;

	private Page<PokerSession> page;

	@Override
	public void init() {
		query = new PokerSessionQuery(repository);
		pageable = PageRequest.of(0, 20);
		page = new PageImpl<>(List.of(valid(PokerSession.class)), pageable, 1);
	}

	@Test
	void should_find_all_sessions_when_type_is_absent() {
		when(repository.findAll(pageable)).thenReturn(page);

		assertThat(query.findAllSessions(null, pageable)).isEqualTo(page);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll(pageable);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_find_sessions_by_type() {
		when(repository.findAllByType(CASH_GAME, pageable)).thenReturn(page);

		assertThat(query.findAllSessions(CASH_GAME, pageable)).isEqualTo(page);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAllByType(CASH_GAME, pageable);
		inOrder.verifyNoMoreInteractions();
	}

}
