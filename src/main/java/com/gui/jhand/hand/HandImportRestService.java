package com.gui.jhand.hand;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/hands")
public class HandImportRestService {

	private final HandImportService importService;

	@PostMapping("/import/batch")
	public ResponseEntity<?> importHandHistories(@RequestParam("files") MultipartFile[] files,
			@RequestParam("heroName") String heroName) {

		Arrays.stream(files).parallel().filter(file -> !file.isEmpty()).forEach(file -> {

			String originalFilename = file.getOriginalFilename();
			String fileName = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);

			try {
				importService.saveAllHandResultsFromFile(new String(file.getBytes(), UTF_8), fileName, heroName);
			}
			catch (IOException e) {
				throw new RuntimeException("Error getting file content: ", e);
			}
		});

		return noContent().build();
	}

}
