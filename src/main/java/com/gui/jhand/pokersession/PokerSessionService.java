package com.gui.jhand.pokersession;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PokerSessionService {

	private final PokerSessionRepository repository;

	public PokerSession save(PokerSession session) {
		return repository.save(session);
	}

}
