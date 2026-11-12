package com.gui.jhand.pokersession;

import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentResultTest {

	@Test
	void should_compute_net_profit_as_prize_minus_buy_in() {
		TournamentResult result = new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(5.67), 1, true, true);

		assertThat(result.netProfit()).isEqualTo(ChipsUtils.of(5.17));
	}

	@Test
	void should_lose_the_buy_in_when_finished_out_of_the_money() {
		TournamentResult result = new TournamentResult(ChipsUtils.of(1.0), ChipsUtils.of(0.0), 236, true, true);

		assertThat(result.netProfit()).isEqualTo(ChipsUtils.of(-1.0));
	}

	@Test
	void should_have_negative_net_profit_while_the_tournament_is_running() {
		TournamentResult result = new TournamentResult(ChipsUtils.of(1.0), ZERO, null, true, false);

		assertThat(result.netProfit()).isEqualTo(ZERO.subtract(ChipsUtils.of(1.0)));
	}

}
