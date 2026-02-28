package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.SEAT_HAND_RESULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SeatSummaryHandlerTest {

	private SeatSummaryHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new SeatSummaryHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(SEAT_HAND_RESULT);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validGuiSeatResultAtBB();
		state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

		int indexBeforeHandle = state.getActivePlayersCount();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getActivePlayersCount()).isEqualTo(indexBeforeHandle + 1);
	}

	@Test
	void should_handle_btn() {
		action = ActionTemplateLoader.validGuiSeatResultAtBtn();
		state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

		int indexBeforeHandle = state.getActivePlayersCount();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getBtnIndex()).isEqualTo(indexBeforeHandle);
		assertThat(state.getActivePlayersCount()).isEqualTo(indexBeforeHandle + 1);
	}

	@Test
	void should_handle_hero_at_btn() {
		action = ActionTemplateLoader.validGuiSeatResultAtBtn();
		state = HandStateTemplateLoader.validGui();

		int indexBeforeHandle = state.getActivePlayersCount();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getBtnIndex()).isEqualTo(indexBeforeHandle);
		assertThat(state.getHeroIndex()).isEqualTo(indexBeforeHandle);
		assertThat(state.getHeroSummaryLine()).isEqualTo(action.getRawLine().toLowerCase());
		assertThat(state.getActivePlayersCount()).isEqualTo(indexBeforeHandle + 1);
	}

}
