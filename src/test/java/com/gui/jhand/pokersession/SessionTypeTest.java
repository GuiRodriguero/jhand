package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTypeTest extends TestBase {

	@Override
	public void init() {

	}

	@ParameterizedTest
	@ValueSource(strings = { "HH20260422 Vinifera - $0.01-$0.02 - USD No Limit Hold'em",
			"HH20260430 Donati - $0.02-$0.05 - USD No Limit Hold'em", "HH20230101 $1 - $2 No Limit Hold'em.txt",
			"HH20230101 $10.00-$20.00 USD.txt" })
	void should_identify_cash_game_session_type(String fileName) {
		assertThat(SessionType.fromFileName(fileName)).isEqualTo(CASH_GAME);
	}

	@ParameterizedTest
	@ValueSource(strings = { "HH20230101 T123456789 No Limit Hold'em.txt",
			"HH20260420 T3993955483 No Limit Hold'em $0.45 + $0.05",
			"HH20260420 T3993955370 No Limit Hold'em $0.90 + $0.10" })
	void should_identify_tournament_session_type(String fileName) {
		assertThat(SessionType.fromFileName(fileName)).isEqualTo(TOURNAMENT);
	}

	@Test
	void should_throw_exception_when_file_name_does_not_match_any_session_type() {
		String invalidFileName = "invalid_file_name.txt";

		assertThatThrownBy(() -> SessionType.fromFileName(invalidFileName)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid or empty file name: " + invalidFileName);
	}

}
