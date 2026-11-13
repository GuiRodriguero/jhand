package com.gui.jhand.hand.stats;

import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.hand.stats.SessionHandStats.PositionResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.hand.Position.BB;
import static com.gui.jhand.hand.Position.BTN;
import static com.gui.jhand.hand.stats.SessionHandTemplateLoader.sessionHand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionHandStatsTest {

	@Test
	void should_convert_session_hands_into_session_stats() {
		SessionHand won = sessionHand("1", BTN, "2.00", true, true, "1.00", "50.00");
		SessionHand lost = sessionHand("2", BB, "3.00", true, false, "-0.40", "-20.00");
		SessionHand folded = sessionHand("3", BTN, "2.61", false, false, "0.00", "0.00");

		SessionHandStats stats = SessionHandStats.of(List.of(won, lost, folded));

		assertThat(stats.points()).extracting(SessionHandStats.Point::handId).containsExactly("1", "2", "3");
		assertThat(stats.points()).extracting(SessionHandStats.Point::cumulativeNetProfit)
			.containsExactly(ChipsUtils.of("1.00"), ChipsUtils.of("0.60"), ChipsUtils.of("0.60"));
		assertThat(stats.points()).extracting(SessionHandStats.Point::cumulativeNetProfitInBb)
			.containsExactly(ChipsUtils.of("50.00"), ChipsUtils.of("30.00"), ChipsUtils.of("30.00"));
		assertThat(stats.points()).extracting(SessionHandStats.Point::startingStack)
			.containsExactly(ChipsUtils.of("2.00"), ChipsUtils.of("3.00"), ChipsUtils.of("2.61"));
		assertThat(stats.points()).extracting(SessionHandStats.Point::stackInBb)
			.containsExactly(ChipsUtils.of("100.00"), ChipsUtils.of("150.00"), ChipsUtils.of("130.50"));
		assertThat(stats.vpipPercentage()).isEqualTo(ChipsUtils.of("66.67"));
		assertThat(stats.pfrPercentage()).isEqualTo(ChipsUtils.of("33.33"));
		assertThat(stats.bbPer100()).isEqualTo(ChipsUtils.of("1000.00"));
		assertThat(stats.bestHand()).isEqualTo(won);
		assertThat(stats.worstHand()).isEqualTo(lost);
		assertThat(stats.resultsByPosition()).containsExactly(
				new PositionResult(BTN, 2, ChipsUtils.of("1.00"), ChipsUtils.of("50.00")),
				new PositionResult(BB, 1, ChipsUtils.of("-0.40"), ChipsUtils.of("-20.00")));
	}

	@Test
	void should_throw_illegal_argument_exception_without_hands() {
		assertThatThrownBy(() -> SessionHandStats.of(List.of())).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("A session has at least one hand");
	}

}
