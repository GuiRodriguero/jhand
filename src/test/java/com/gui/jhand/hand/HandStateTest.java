package com.gui.jhand.hand;

import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class HandStateTest {

	private HandState handState;

	@BeforeEach
	void setUp() {
		handState = new HandState("hero", "My Session");
	}

	@Test
	void should_instantiate() {
		assertThat(handState).isEqualTo(new HandState("hero", "My Session"));
	}

	@Test
	void should_update_current_street() {
		HandState expected = new HandState("hero", "My Session");
		expected.addCurrentStreetInvestment(ZERO);

		handState.updateCurrentStreet(expected.getCurrentStreet());

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_total_invested() {
		HandState expected = new HandState("hero", "My Session");
		expected.addTotalInvested(expected.getTotalInvested().add(ChipsUtils.of(200.0)));

		handState.addTotalInvested(ChipsUtils.of(200.0));

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_current_street_investment() {
		HandState expected = new HandState("hero", "My Session");
		expected.addCurrentStreetInvestment(expected.getCurrentStreetInvestment().add(ChipsUtils.of(400.0)));

		handState.addCurrentStreetInvestment(ChipsUtils.of(400.0));

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_add_total_collected() {
		HandState expected = new HandState("hero", "My Session");
		expected.addTotalCollected(expected.getTotalCollected().add(ChipsUtils.of(300.0)));

		handState.addTotalCollected(ChipsUtils.of(300.0));

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_subtract_total_invested() {
		HandState expected = new HandState("hero", "My Session");
		expected.addTotalInvested(expected.getTotalInvested().subtract(ChipsUtils.of(700.0)));

		handState.subtractTotalInvested(ChipsUtils.of(700.0));

		assertThat(handState).isEqualTo(expected);
	}

	@Test
	void should_get_net_profit() {
		handState.addTotalCollected(ChipsUtils.of(1000.0));
		handState.addTotalInvested(ChipsUtils.of(500.0));

		assertThat(handState.getNetProfit()).isEqualTo(ChipsUtils.of(500.0));
	}

}
