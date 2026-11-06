package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.PokerSessionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.gui.jhand.TestBase.valid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokerSessionRestService.class)
class PokerSessionRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PokerSessionQuery query;

	@Test
	void should_get_sessions() throws Exception {
		PokerSession session = valid(PokerSession.class);
		when(query.findAllSessions(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(session)));

		mockMvc.perform(get("/v1/sessions"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].sessionId").value(session.getSessionId()))
			.andExpect(jsonPath("$.content[0].type").value(session.getType().name()));

		verify(query).findAllSessions(argThat(pageable -> pageable.getPageSize() == 20
				&& pageable.getSort().getOrderFor("sessionId").getDirection() == DESC));
	}

}
