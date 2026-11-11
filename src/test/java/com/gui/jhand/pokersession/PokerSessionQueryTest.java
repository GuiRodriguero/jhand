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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class PokerSessionQueryTest extends TestBase {

	@Mock
	private PokerSessionRepository repository;

	private PokerSessionQuery query;

	@Override
	public void init() {
		query = new PokerSessionQuery(repository);
	}

	@Test
	void should_find_all_sessions() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<PokerSession> page = new PageImpl<>(List.of(valid(PokerSession.class)), pageable, 1);
		when(repository.findAll(pageable)).thenReturn(page);

		assertThat(query.findAllSessions(pageable)).isEqualTo(page);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll(pageable);
		inOrder.verifyNoMoreInteractions();
	}

}
