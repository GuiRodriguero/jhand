package com.gui.jhand.evaluator;

import com.gui.jhand.core.poker.HandRank;

public record HandEvaluationResult(int score, HandRank handRank) {

	public static HandEvaluationResult of(int score) {
		return new HandEvaluationResult(score, HandRank.fromScore(score));
	}

}
