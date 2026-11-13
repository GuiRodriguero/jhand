package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.TournamentPokerSession;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class TournamentSessionResponseTest {

	@Test
	void should_map_all_fields_from_tournament_session() {
		TournamentPokerSession entity = valid(TournamentPokerSession.class);

		assertThat(TournamentSessionResponse.of(entity)).isEqualTo(new TournamentSessionResponse(entity.getSessionId(),
				entity.getType().name(), entity.getHandCount(), entity.getStartTime(), entity.getEndTime(),
				entity.getNetProfit(), entity.getBuyIn(), entity.getPrize(), entity.getFinishPosition(),
				entity.getRealMoney(), entity.getOutcome().name()));
	}

}
