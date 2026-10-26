package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import com.gui.jhand.pokersession.PokerSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HandImportServiceTest extends TestBase {

	private static final String FILE_NAME = "My Session";

	@Mock
	private HandResultBuilder builder;

	@Mock
	private HandResultRepository repository;

	@Mock
	private PokerSessionService pokerSessionService;

	private HandImportService service;

	private String rawHands;

	@Override
	public void init() {
		service = new HandImportService(builder, repository, pokerSessionService);
		rawHands = HandTemplateLoader.heroAtBtn().concat("\n\n\n").concat(HandTemplateLoader.heroAtSB());
	}

	@Test
	void should_save_all_hand_results_from_file() {
		when(builder.build(anyList(), anyString(), anyString())).thenReturn(valid(HandResult.class));

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, FILE_NAME, "GuiRodri2013"))
			.doesNotThrowAnyException();

		InOrder inOrder = inOrder(builder, repository, pokerSessionService);
		inOrder.verify(builder, times(2)).build(anyList(), eq(FILE_NAME), eq("GuiRodri2013"));
		inOrder.verify(repository).saveAll(anyList());
		inOrder.verify(pokerSessionService).create(FILE_NAME, ZERO);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void should_do_nothing_if_no_hands_in_file() {
		rawHands = "";
		when(builder.build(anyList(), anyString(), anyString())).thenReturn(valid(HandResult.class));

		assertThatCode(() -> service.saveAllHandResultsFromFile(rawHands, FILE_NAME, "GuiRodri2013"))
			.doesNotThrowAnyException();

		InOrder inOrder = inOrder(builder, repository, pokerSessionService);
		inOrder.verify(builder, never()).build(anyList(), any(), any());
		inOrder.verify(repository, never()).saveAll(anyList());
		inOrder.verify(pokerSessionService, never()).create(any(), any());
		inOrder.verifyNoMoreInteractions();
	}

}
