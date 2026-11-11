package com.gui.jhand.action.handler;

import com.gui.jhand.TestBase;
import com.gui.jhand.action.Action;
import com.gui.jhand.hand.HandState;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.BIG_BLIND_LEVEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class BigBlindLevelHandlerTest extends TestBase {

	private BigBlindLevelHandler handler;

	private Action action;

	private HandState state;

	@Override
	public void init() {
		handler = new BigBlindLevelHandler();
		action = valid(Action.class);
		state = valid(HandState.class);
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(BIG_BLIND_LEVEL);
	}

	@Test
	void should_handle() {
		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getBlindValue()).isEqualTo(action.getAmount());
	}

}
