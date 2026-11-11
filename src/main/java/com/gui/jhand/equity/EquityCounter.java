package com.gui.jhand.equity;

final class EquityCounter {

	private int heroWins = 0;

	private int villainWins = 0;

	private int ties = 0;

	void add(EquityEvaluationWinner winner) {
		switch (winner) {
			case HERO -> heroWins++;
			case VILLAIN -> villainWins++;
			case TIE -> ties++;
		}
	}

	EquityResult toResult() {
		return EquityResult.of(heroWins, villainWins, ties);
	}

}