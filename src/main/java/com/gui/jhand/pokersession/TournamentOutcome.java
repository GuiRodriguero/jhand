package com.gui.jhand.pokersession;

import java.math.BigDecimal;

public enum TournamentOutcome {

	WINNER, IN_THE_MONEY, OUT_OF_THE_MONEY;

	public static TournamentOutcome of(BigDecimal prize, Integer finishPosition) {
		if (Integer.valueOf(1).equals(finishPosition)) {
			return WINNER;
		}
		return prize.signum() > 0 ? IN_THE_MONEY : OUT_OF_THE_MONEY;
	}

}
