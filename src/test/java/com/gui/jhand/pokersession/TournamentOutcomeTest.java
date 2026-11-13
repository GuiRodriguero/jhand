package com.gui.jhand.pokersession;

import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.pokersession.TournamentOutcome.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentOutcomeTest {

	@Test
	void should_be_winner_when_hero_finished_first() {
		assertThat(TournamentOutcome.of(ChipsUtils.of(5.67), 1)).isEqualTo(WINNER);
	}

	@Test
	void should_be_in_the_money_when_hero_received_a_prize() {
		assertThat(TournamentOutcome.of(ChipsUtils.of(3.70), 2)).isEqualTo(IN_THE_MONEY);
	}

	@Test
	void should_be_out_of_the_money_when_hero_finished_without_prize() {
		assertThat(TournamentOutcome.of(ZERO, 9)).isEqualTo(OUT_OF_THE_MONEY);
	}

}
