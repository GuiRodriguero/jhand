package com.gui.jhand.pokersession;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PokerSessionQuery {

	private final PokerSessionRepository repository;

	public Page<PokerSession> findAllSessions(SessionType type, Pageable pageable) {
		return type == null ? repository.findAll(pageable) : repository.findAllByType(type, pageable);
	}

}
