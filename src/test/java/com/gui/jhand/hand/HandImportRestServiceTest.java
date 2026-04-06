package com.gui.jhand.hand;

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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
