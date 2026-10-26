package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.assertj.core.api.Assertions.assertThat;

class PokerSessionTest extends TestBase {

	@Override
	public void init() {

	}

	@Test
	void should_create_poker_session_from_cash_game_file_name() {
		String fileName = "HH20230101 $0.01-$0.02 USD.txt";
		BigDecimal netProfit = BigDecimal.valueOf(15.50);

		assertThat(PokerSession.of(fileName, netProfit)).isEqualTo(PokerSession.of(fileName, CASH_GAME, netProfit));
	}

	@Test
	void should_create_poker_session_from_tournament_file_name() {
		String fileName = "HH20230101 T123456789.txt";
		BigDecimal netProfit = BigDecimal.valueOf(-5.00);

		assertThat(PokerSession.of(fileName, netProfit)).isEqualTo(PokerSession.of(fileName, TOURNAMENT, netProfit));
	}

}
