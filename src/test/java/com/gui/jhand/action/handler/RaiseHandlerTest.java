package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.ACTION_RAISE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class RaiseHandlerTest {

	private RaiseHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new RaiseHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(ACTION_RAISE);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validGuiRaise();
		state = HandStateTemplateLoader.validGuiFlop();

		boolean isPfrBeforeHandle = state.isPfr();
		boolean isVpipBeforeHandle = state.isVpip();
		double totalInvestedBeforeHandle = state.getTotalInvested();
		double actualCost = action.getAmount() - state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle + actualCost);
		assertThat(state.getCurrentStreetInvestment()).isEqualTo(action.getAmount());
		assertThat(state.isPfr()).isEqualTo(isPfrBeforeHandle);
		assertThat(state.isVpip()).isEqualTo(isVpipBeforeHandle);
	}

	@Test
	void should_handle_pfr() {
		action = ActionTemplateLoader.validGuiRaise();
		state = HandStateTemplateLoader.validGuiPreFlop();

		double totalInvestedBeforeHandle = state.getTotalInvested();
		double actualCost = action.getAmount() - state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle + actualCost);
		assertThat(state.getCurrentStreetInvestment()).isEqualTo(action.getAmount());
		assertThat(state.isPfr()).isTrue();
		assertThat(state.isVpip()).isTrue();
	}

	@Test
	void should_not_handle_when_player_is_different() {
		state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

		boolean isPfrBeforeHandle = state.isPfr();
		boolean isVpipBeforeHandle = state.isVpip();
		double totalInvestedBeforeHandle = state.getTotalInvested();
		double currentStreetInvestedBeforeHandle = state.getCurrentStreetInvestment();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle);
		assertThat(state.getCurrentStreetInvestment()).isEqualTo(currentStreetInvestedBeforeHandle);
		assertThat(state.isPfr()).isEqualTo(isPfrBeforeHandle);
		assertThat(state.isVpip()).isEqualTo(isVpipBeforeHandle);
	}

}
