package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
		page = new PageImpl<>(List.of(valid(TournamentPokerSession.class)), pageable, 1);
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
		page = new PageImpl<>(List.of(valid(CashPokerSession.class)), pageable, 1);
		when(repository.findAllByType(CASH_GAME, pageable)).thenReturn(page);

		assertThat(query.findAllSessions(CASH_GAME, pageable)).isEqualTo(page);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAllByType(CASH_GAME, pageable);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_find_session_by_id() {
		PokerSession session = valid(TournamentPokerSession.class);
		when(repository.findById(session.getSessionId())).thenReturn(Optional.of(session));

		assertThat(query.findById(session.getSessionId())).isEqualTo(session);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById(session.getSessionId());
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_throw_not_found_exception_when_session_does_not_exist() {
		when(repository.findById("Unknown")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> query.findById("Unknown")).isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Session Unknown not found");

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findById("Unknown");
		inOrder.verifyNoMoreInteractions();
	}

}
