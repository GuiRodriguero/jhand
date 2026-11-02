package com.gui.jhand.equity;

public record EquityResult(double heroEquity, double villainEquity, double tieProbability, int heroWins,
		int villainWins, int ties, int totalScenarios) {

	public static EquityResult of(int heroWins, int villainWins, int ties) {
		int total = heroWins + villainWins + ties;
		double heroEq = (heroWins + (ties * 0.5)) / total;
		double villainEq = (villainWins + (ties * 0.5)) / total;
		double tieProb = (double) ties / total;
		return new EquityResult(heroEq, villainEq, tieProb, heroWins, villainWins, ties, total);
	}

}