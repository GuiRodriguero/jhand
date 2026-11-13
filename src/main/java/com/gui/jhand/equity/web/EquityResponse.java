package com.gui.jhand.equity.web;

import com.gui.jhand.equity.EquityResult;

public record EquityResponse(double heroEquity, double villainEquity, double tieProbability, int totalScenarios) {

	public static EquityResponse of(EquityResult result) {
		return new EquityResponse(result.heroEquity(), result.villainEquity(), result.tieProbability(),
				result.totalScenarios());
	}

}
