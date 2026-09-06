package com.gui.jhand.hand;

import com.gui.jhand.hand.web.HandResultFilter;
import com.gui.jhand.hand.web.HandResultSummaryResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gui.jhand.hand.HandResultSpecification.withFilters;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HandResultQuery {

	private final HandResultRepository repository;

	public Page<HandResultSummaryResponse> getHandsPaginated(HandResultFilter filter, Pageable pageable) {
		return repository.findAll(withFilters(filter), pageable).map(HandResultSummaryResponse::of);
	}

}
