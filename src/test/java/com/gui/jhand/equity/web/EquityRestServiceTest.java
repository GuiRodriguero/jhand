package com.gui.jhand.equity.web;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import com.gui.jhand.equity.EquityCalculator;
import com.gui.jhand.equity.EquityResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquityRestService.class)
class EquityRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EquityCalculator calculator;

	@Test
	void should_calculate_equity() throws Exception {
		when(calculator.calculate(any(), any(), any())).thenReturn(EquityResult.of(3, 1, 0));

		mockMvc.perform(post("/v1/equity").contentType(APPLICATION_JSON).content("""
				{"heroCards": "As Kh", "villainCards": "Qc Qd", "board": "2c 7d Th"}
				"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.heroEquity").value(0.75))
			.andExpect(jsonPath("$.villainEquity").value(0.25))
			.andExpect(jsonPath("$.tieProbability").value(0.0))
			.andExpect(jsonPath("$.totalScenarios").value(4));

		verify(calculator).calculate(PocketCards.fromHandHistory("As Kh"), PocketCards.fromHandHistory("Qc Qd"),
				Board.fromString("2c 7d Th"));
	}

	@Test
	void should_calculate_pre_flop_equity_without_board() throws Exception {
		when(calculator.calculate(any(), any(), any())).thenReturn(EquityResult.of(1, 1, 0));

		mockMvc.perform(post("/v1/equity").contentType(APPLICATION_JSON).content("""
				{"heroCards": "As Kh", "villainCards": "Qc Qd"}
				""")).andExpect(status().isOk());

		verify(calculator).calculate(PocketCards.fromHandHistory("As Kh"), PocketCards.fromHandHistory("Qc Qd"),
				Board.of());
	}

	@Test
	void should_return_bad_request_for_invalid_card() throws Exception {
		mockMvc.perform(post("/v1/equity").contentType(APPLICATION_JSON).content("""
				{"heroCards": "As Xh", "villainCards": "Qc Qd"}
				""")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Bad Request"));

		verifyNoInteractions(calculator);
	}

	@Test
	void should_return_bad_request_when_calculator_rejects_input() throws Exception {
		when(calculator.calculate(any(), any(), any()))
			.thenThrow(new IllegalArgumentException("Duplicated cards: the same card cannot be used twice"));

		mockMvc.perform(post("/v1/equity").contentType(APPLICATION_JSON).content("""
				{"heroCards": "As Kh", "villainCards": "As Qd"}
				"""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.detail").value("Duplicated cards: the same card cannot be used twice"));
	}

}
