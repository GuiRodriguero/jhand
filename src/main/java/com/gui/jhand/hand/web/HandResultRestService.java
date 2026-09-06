package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResultQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/hands")
class HandResultRestService {

	private final HandResultQuery query;

	@GetMapping
	public ResponseEntity<Page<HandResultSummaryResponse>> getHands(@ModelAttribute HandResultFilter filter,
			@PageableDefault(size = 20, sort = "time", direction = DESC) Pageable pageable) {
		return ResponseEntity.ok(query.getHandsPaginated(filter, pageable));
	}

}
