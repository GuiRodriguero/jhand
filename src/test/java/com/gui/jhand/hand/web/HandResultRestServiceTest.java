package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResultQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HandResultRestService.class)
class HandResultRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private HandResultQuery query;

	@Test
	void should_get_hands() throws Exception {
		mockMvc.perform(get("/v1/hands")).andExpect(status().isOk());
		verify(query).getHandsPaginated(any(HandResultFilter.class), any(Pageable.class));
	}

}
