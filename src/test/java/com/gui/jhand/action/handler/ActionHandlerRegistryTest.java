package com.gui.jhand.action.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static com.gui.jhand.action.ActionType.ACTION_FOLD;
import static com.gui.jhand.action.ActionType.DEALT_TO_HERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ActionHandlerRegistryTest {

	@Mock
	private ActionHandler handler;

	private ActionHandlerRegistry registry;

	@BeforeEach
	void setUp() {
		handler = Mockito.mock(ActionHandler.class);
		when(handler.getSupportedTypes()).thenReturn(List.of(DEALT_TO_HERO));

		List<ActionHandler> handlers = List.of(handler);

		registry = new ActionHandlerRegistry(handlers);
	}

	@Test
	void should_instantiate() {
		assertThat(registry.getHandler(DEALT_TO_HERO)).isEqualTo(handler);
	}

	@Test
	void should_get_handler() {
		assertThat(registry.getHandler(DEALT_TO_HERO)).isEqualTo(handler);
	}

	@Test
	void should_get_handler_for_unknown_action() {
		assertThat(registry.getHandler(ACTION_FOLD)).isNull();
	}

}