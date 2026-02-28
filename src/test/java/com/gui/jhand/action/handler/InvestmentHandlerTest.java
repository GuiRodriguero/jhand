package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class InvestmentHandlerTest {

	private InvestmentHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new InvestmentHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(POST_BLIND, ACTION_CALL, ACTION_BET);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validGuiPostBlind();
		state = HandStateTemplateLoader.validGuiPreFlop();

		boolean isVpipBeforeHandle = state.isVpip();
		double totalInvestedBeforeHandle = state.getTotalInvested();
		double currentStreetInvestedBeforeHandle = state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle + action.getAmount());
		assertThat(state.getCurrentStreetInvestment())
			.isEqualTo(currentStreetInvestedBeforeHandle + action.getAmount());
		assertThat(state.isVpip()).isEqualTo(isVpipBeforeHandle);
	}

	@Test
	void should_handle_with_vpip() {
		action = ActionTemplateLoader.validGuiCall();
		state = HandStateTemplateLoader.validGuiPreFlop();

		double totalInvestedBeforeHandle = state.getTotalInvested();
		double currentStreetInvestedBeforeHandle = state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle + action.getAmount());
		assertThat(state.getCurrentStreetInvestment())
			.isEqualTo(currentStreetInvestedBeforeHandle + action.getAmount());
		assertThat(state.isVpip()).isTrue();
	}

	@Test
	void should_not_handle() {
		state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

		boolean isVpipBeforeHandle = state.isVpip();
		double totalInvestedBeforeHandle = state.getTotalInvested();
		double currentStreetInvestedBeforeHandle = state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle);
		assertThat(state.getCurrentStreetInvestment()).isEqualTo(currentStreetInvestedBeforeHandle);
		assertThat(state.isVpip()).isEqualTo(isVpipBeforeHandle);
	}

}
