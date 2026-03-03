package com.gui.jhand.hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandStateTest {

	private HandState handState;

	@BeforeEach
	void setUp() {
		handState = new HandState("hero");
	}

	@Test
	void should_instantiate() {
		assertThat(handState).isEqualTo(new HandState("hero"));
	}

	@Test
	void should_update_current_street() {
		HandState expected = new HandState("hero");
		expected.addCurrentStreetInvestment(0.0);

		handState.updateCurrentStreet(expected.getCurrentStreet());

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_total_invested() {
		HandState expected = new HandState("hero");
		expected.addTotalInvested(expected.getTotalInvested() + 200.0);

		handState.addTotalInvested(200.0);

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_current_street_investment() {
		HandState expected = new HandState("hero");
		expected.addCurrentStreetInvestment(expected.getCurrentStreetInvestment() + 400.0);

		handState.addCurrentStreetInvestment(400.0);

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_total_collected() {
		HandState expected = new HandState("hero");
		expected.addTotalCollected(expected.getTotalCollected() + 300.0);

		handState.addTotalCollected(300.0);

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_subtract_total_invested() {
		HandState expected = new HandState("hero");
		expected.addTotalInvested(expected.getTotalInvested() - 700.0);

		handState.subtractTotalInvested(700.0);

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_get_net_profit() {
		handState.addTotalCollected(1000.0);
		handState.addTotalInvested(500.0);

		assertThat(handState.getNetProfit()).isEqualTo(500.0);
	}

}
