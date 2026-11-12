package com.gui.jhand.pokersession;

import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentPokerSessionTest {

	private static final Instant START_TIME = Instant.ofEpochSecond(1771774381);

	private static final Instant END_TIME = START_TIME.plusSeconds(3600);

	@Test
	void should_create_finished_tournament_with_prize_minus_buy_in() {
		TournamentResult result = new TournamentResult(ChipsUtils.of(0.50), ChipsUtils.of(5.67), 1, true, true);

		TournamentPokerSession session = TournamentPokerSession.of("My Tournament", 42, START_TIME, END_TIME, result);

		assertThat(session.getSessionId()).isEqualTo("My Tournament");
		assertThat(session.getType()).isEqualTo(TOURNAMENT);
		assertThat(session.getHandCount()).isEqualTo(42);
		assertThat(session.getStartTime()).isEqualTo(START_TIME);
		assertThat(session.getEndTime()).isEqualTo(END_TIME);
		assertThat(session.getNetProfit()).isEqualTo(ChipsUtils.of(5.17));
		assertThat(session.getBuyIn()).isEqualTo(ChipsUtils.of(0.50));
		assertThat(session.getPrize()).isEqualTo(ChipsUtils.of(5.67));
		assertThat(session.getFinishPosition()).isEqualTo(1);
		assertThat(session.getRealMoney()).isTrue();
	}

	@Test
	void should_create_running_tournament_without_result_nor_prize() {
		TournamentResult result = new TournamentResult(ChipsUtils.of(0.50), ZERO, null, false, false);

		TournamentPokerSession session = TournamentPokerSession.of("My Tournament", 42, START_TIME, END_TIME, result);

		assertThat(session.getNetProfit()).isEqualTo(ChipsUtils.of(-0.50));
		assertThat(session.getPrize()).isNull();
		assertThat(session.getFinishPosition()).isNull();
		assertThat(session.getBuyIn()).isEqualTo(ChipsUtils.of(0.50));
		assertThat(session.getRealMoney()).isFalse();
	}

}
