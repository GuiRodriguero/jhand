package com.gui.jhand.hand.web;

import com.gui.jhand.core.exception.BusinessException;
import com.gui.jhand.hand.HandImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.gui.jhand.hand.HandTemplateLoader.validWonWithShowdown;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HandImportRestService.class)
class HandImportRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private HandImportService importService;

	private final String heroName = "GuiRodri2013";

	@Test
	void should_import_hand_histories() throws Exception {
		MockMultipartFile fakeFile = new MockMultipartFile("files", "hand_history.txt", "text/plain",
				validWonWithShowdown().getBytes());

		String originalFilename = fakeFile.getOriginalFilename();
		String fileName = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);

		mockMvc.perform(multipart("/v1/hands/import/batch").file(fakeFile).param("heroName", heroName))
			.andExpect(status().isNoContent());

		verify(importService).saveAllHandResultsFromFile(anyString(), eq(fileName), eq(heroName));
	}

	@Test
	void should_skip_empty_files() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile("files", "empty.txt", "text/plain", new byte[0]);
		MockMultipartFile fakeFile = new MockMultipartFile("files", "hand_history.txt", "text/plain",
				validWonWithShowdown().getBytes());

		mockMvc.perform(multipart("/v1/hands/import/batch").file(emptyFile).file(fakeFile).param("heroName", heroName))
			.andExpect(status().isNoContent());

		verify(importService).saveAllHandResultsFromFile(anyString(), eq("hand_history.txt"), eq(heroName));
		verifyNoMoreInteractions(importService);
	}

	@Test
	void should_return_unprocessable_content_when_import_violates_business_rule() throws Exception {
		MockMultipartFile fakeFile = new MockMultipartFile("files", "hand_history.txt", "text/plain",
				validWonWithShowdown().getBytes());
		doThrow(new BusinessException("Hand 123 has no valid big blind value: null")).when(importService)
			.saveAllHandResultsFromFile(anyString(), anyString(), anyString());

		mockMvc.perform(multipart("/v1/hands/import/batch").file(fakeFile).param("heroName", heroName))
			.andExpect(status().isUnprocessableContent())
			.andExpect(jsonPath("$.detail").value("Hand 123 has no valid big blind value: null"));
	}

	@Test
	void should_return_bad_request_when_hero_name_is_missing() throws Exception {
		MockMultipartFile fakeFile = new MockMultipartFile("files", "hand_history.txt", "text/plain",
				validWonWithShowdown().getBytes());

		mockMvc.perform(multipart("/v1/hands/import/batch").file(fakeFile)).andExpect(status().isBadRequest());
	}

	@Test
	void should_throw_runtime_exception_when_import_hand_histories() throws Exception {
		MockMultipartFile fakeFile = new MockMultipartFile("files", "hand_history.txt", "text/plain",
				"any content".getBytes()) {
			@Override
			public byte[] getBytes() throws IOException {
				throw new IOException("Simulated error");
			}
		};

		mockMvc.perform(multipart("/v1/hands/import/batch").file(fakeFile).param("heroName", heroName))
			.andExpect(status().isInternalServerError());
	}

}
