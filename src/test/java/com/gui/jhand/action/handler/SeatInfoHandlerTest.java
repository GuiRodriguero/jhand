package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.gui.jhand.action.ActionType.SEAT_INFO;
import static com.gui.jhand.hand.HandStateTemplateLoader.validHeroDifferentFromAction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SeatInfoHandlerTest {

	private SeatInfoHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new SeatInfoHandler();
		action = Instancio.of(Action.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(SEAT_INFO);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validGuiSeatInfo();
		state = HandStateTemplateLoader.validGui();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getStartingStack()).isEqualTo(action.getAmount());
	}

	@Test
	void should_not_handle_when_player_is_different() {
		state = validHeroDifferentFromAction(action.getPlayerName());

		BigDecimal startingStack = state.getStartingStack();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getStartingStack()).isEqualTo(startingStack);
	}

}
