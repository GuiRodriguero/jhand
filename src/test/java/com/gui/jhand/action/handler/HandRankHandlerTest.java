package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.hand.HandState;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.HAND_RANK;
import static com.gui.jhand.core.poker.HandRank.fromRawHand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class HandRankHandlerTest {

	private HandRankHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new HandRankHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(HAND_RANK);
	}

	@Test
	void should_handle() {
		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getHandRank()).isEqualTo(fromRawHand(action.getMainInformation()));
	}

}
