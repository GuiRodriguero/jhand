package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSessionQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/sessions")
class PokerSessionRestService {

	private final PokerSessionQuery query;

	@GetMapping
	public ResponseEntity<Page<PokerSessionResponse>> findAll(
			@PageableDefault(size = 20, sort = "sessionId", direction = DESC) Pageable pageable) {
		return ResponseEntity.ok(query.findAllSessions(pageable).map(PokerSessionResponse::of));
	}

}
