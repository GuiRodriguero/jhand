package com.gui.jhand.equity;

enum EquityEvaluationWinner {

	HERO, VILLAIN, TIE;

	static EquityEvaluationWinner resolveWinner(int heroScore, int villainScore) {
		if (heroScore < villainScore) {
			return HERO;
		}
		else if (heroScore > villainScore) {
			return VILLAIN;
		}
		return TIE;
	}

}
