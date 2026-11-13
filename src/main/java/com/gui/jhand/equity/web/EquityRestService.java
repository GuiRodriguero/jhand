package com.gui.jhand.equity.web;

import com.gui.jhand.equity.EquityCalculator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/equity")
class EquityRestService {

	private final EquityCalculator calculator;

	@PostMapping
	public ResponseEntity<EquityResponse> calculate(@RequestBody EquityRequest request) {
		return ResponseEntity.ok(EquityResponse
			.of(calculator.calculate(request.toHeroCards(), request.toVillainCards(), request.toBoard())));
	}

}
