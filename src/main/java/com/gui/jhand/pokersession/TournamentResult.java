package com.gui.jhand.pokersession;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public record TournamentResult(BigDecimal buyIn, BigDecimal prize, Integer finishPosition, boolean realMoney,
		boolean finished) {

	public BigDecimal netProfit() {
		return finished ? prize.subtract(buyIn) : ZERO.subtract(buyIn);
	}

}
