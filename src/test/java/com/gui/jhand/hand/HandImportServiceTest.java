package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HandImportServiceTest extends TestBase {

	@Mock
	private HandResultBuilder builder;

	@Mock
	private HandResultRepository repository;

	private HandImportService service;

	private String rawHands;

	@Override
	public void init() {
		service = new HandImportService(builder, repository);
		rawHands = HandTemplateLoader.heroAtBtn().concat("\n\n\n").concat(HandTemplateLoader.heroAtSB());
	}

	@Test
	void should_save_all_hand_results_from_file() {
		when(builder.build(anyList(), anyString(), anyString())).thenReturn(valid(HandResult.class));

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, "My Session", "GuiRodri2013"))
			.doesNotThrowAnyException();

		InOrder inOrder = inOrder(builder, repository);
		inOrder.verify(builder, times(2)).build(anyList(), eq("My Session"), eq("GuiRodri2013"));
		inOrder.verify(repository).saveAll(anyList());
		inOrder.verifyNoMoreInteractions();
	}

}
