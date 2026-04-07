package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.BOARD;
import static com.gui.jhand.core.poker.Board.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class BoardHandlerTest {

	private BoardHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new BoardHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(BOARD);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validBoard();
		state = HandStateTemplateLoader.validGui();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getBoard()).isEqualTo(fromString(action.getMainInformation()));
	}

}
