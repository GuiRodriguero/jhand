package com.gui.jhand.hand.stats.web;

import com.gui.jhand.hand.stats.SessionHand;
import com.gui.jhand.hand.stats.SessionHandStats;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record SessionHandStatsResponse(List<PointResponse> points, BigDecimal vpipPercentage, BigDecimal pfrPercentage,
		BigDecimal bbPer100, HandResponse bestHand, HandResponse worstHand, List<PositionResponse> byPosition) {

	public record PointResponse(String handId, Instant time, BigDecimal netProfit, BigDecimal netProfitInBb,
			BigDecimal cumulativeNetProfit, BigDecimal cumulativeNetProfitInBb, BigDecimal startingStack,
			BigDecimal stackInBb) {

		static PointResponse of(SessionHandStats.Point point) {
			return new PointResponse(point.handId(), point.time(), point.netProfit(), point.netProfitInBb(),
					point.cumulativeNetProfit(), point.cumulativeNetProfitInBb(), point.startingStack(),
					point.stackInBb());
		}

	}

	public record HandResponse(String handId, Instant time, String heroCards, String position, String handRank,
			BigDecimal netProfit, BigDecimal netProfitInBb) {

		static HandResponse of(SessionHand hand) {
			return new HandResponse(hand.handId(), hand.time(), Objects.toString(hand.heroCards(), null),
					Objects.toString(hand.position(), null), Objects.toString(hand.handRank(), null), hand.netProfit(),
					hand.netProfitInBb());
		}

	}

	public record PositionResponse(String position, int hands, BigDecimal netProfit, BigDecimal netProfitInBb) {

		static PositionResponse of(SessionHandStats.PositionResult result) {
			return new PositionResponse(result.position().name(), result.hands(), result.netProfit(),
					result.netProfitInBb());
		}

	}

	static SessionHandStatsResponse of(SessionHandStats stats) {
		return new SessionHandStatsResponse(stats.points().stream().map(PointResponse::of).toList(),
				stats.vpipPercentage(), stats.pfrPercentage(), stats.bbPer100(), HandResponse.of(stats.bestHand()),
				HandResponse.of(stats.worstHand()), stats.resultsByPosition().stream().map(PositionResponse::of).toList());
	}

}
