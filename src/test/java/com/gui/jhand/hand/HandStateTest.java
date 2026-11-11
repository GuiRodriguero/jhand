package com.gui.jhand.hand;

import com.gui.jhand.core.exception.BusinessException;
import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@Test
	void should_get_net_profit_in_bb() {
		handState.setBlindValue(ChipsUtils.of(20.0));
		handState.addTotalCollected(ChipsUtils.of(1000.0));
		handState.addTotalInvested(ChipsUtils.of(500.0));

		assertThat(handState.getNetProfitInBb()).isEqualTo(ChipsUtils.of(25.0));
	}

	@Test
	void should_round_net_profit_in_bb() {
		handState.setBlindValue(ChipsUtils.of(0.02));
		handState.addTotalInvested(ChipsUtils.of(0.05));

		assertThat(handState.getNetProfitInBb()).isEqualTo(ChipsUtils.of(-2.5));
	}

	@Test
	void should_round_half_up_net_profit_in_bb() {
		handState.setBlindValue(ChipsUtils.of(600.0));
		handState.addTotalInvested(ChipsUtils.of(2555.0));

		assertThat(handState.getNetProfitInBb()).isEqualTo(ChipsUtils.of(-4.26));
	}

	@Test
	void should_throw_business_exception_when_blind_value_is_null() {
		handState.setHandId("123");
		handState.setBlindValue(null);

		assertThatThrownBy(handState::getNetProfitInBb).isInstanceOf(BusinessException.class)
			.hasMessage("Hand 123 has no valid big blind value: null");
	}

	@Test
	void should_throw_business_exception_when_blind_value_is_default_zero() {
		handState.addTotalCollected(ChipsUtils.of(1000.0));

		assertThatThrownBy(handState::getNetProfitInBb).isInstanceOf(BusinessException.class);
	}

	@ParameterizedTest
	@ValueSource(doubles = { 0.0, -0.02 })
	void should_throw_business_exception_when_blind_value_is_not_positive(double blindValue) {
		handState.setBlindValue(ChipsUtils.of(blindValue));

		assertThatThrownBy(handState::getNetProfitInBb).isInstanceOf(BusinessException.class);
	}

}
