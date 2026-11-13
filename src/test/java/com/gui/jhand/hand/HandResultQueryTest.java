package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import com.gui.jhand.hand.web.HandResultFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class HandResultQueryTest extends TestBase {

	@Mock
	private HandResultRepository repository;

	private HandResultQuery query;

	@Override
	public void init() {
		query = new HandResultQuery(repository);
	}

	@Test
	void should_get_hands_paginated() {
		HandResultFilter filter = valid(HandResultFilter.class);
		List<HandResult> validHands = List.of(valid(HandResult.class));
		Pageable pageable = Pageable.unpaged();
		Page<HandResult> page = new PageImpl<>(validHands);

		when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

		assertThat(query.getHandsPaginated(filter, pageable)).isEqualTo(page);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAll(any(Specification.class), any(Pageable.class));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_find_all_hands_of_a_session() {
		List<HandResult> hands = List.of(valid(HandResult.class));
		when(repository.findAllBySessionIdOrderByTimeAscHandIdAsc("My Session")).thenReturn(hands);

		assertThat(query.findAllBySessionId("My Session")).isEqualTo(hands);

		InOrder inOrder = inOrder(repository);
		inOrder.verify(repository).findAllBySessionIdOrderByTimeAscHandIdAsc("My Session");
		inOrder.verifyNoMoreInteractions();
	}

}
