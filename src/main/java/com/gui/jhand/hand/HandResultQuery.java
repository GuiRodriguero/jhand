package com.gui.jhand.hand;

import com.gui.jhand.hand.web.HandResultFilter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gui.jhand.hand.HandResultSpecification.withFilters;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HandResultQuery {

	private final HandResultRepository repository;

	public Page<HandResult> getHandsPaginated(HandResultFilter filter, Pageable pageable) {
		return repository.findAll(withFilters(filter), pageable);
	}

	public List<HandResult> findAllBySessionId(String sessionId) {
		return repository.findAllBySessionIdOrderByTimeAscHandIdAsc(sessionId);
	}

}
