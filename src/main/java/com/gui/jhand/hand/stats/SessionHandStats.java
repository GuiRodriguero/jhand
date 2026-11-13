package com.gui.jhand.hand.stats;

import com.gui.jhand.hand.Position;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public record SessionHandStats(List<Point> points, BigDecimal vpipPercentage, BigDecimal pfrPercentage,
		BigDecimal bbPer100, SessionHand bestHand, SessionHand worstHand, List<PositionResult> resultsByPosition) {

	private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

	private static final Comparator<SessionHand> BY_NET_PROFIT_IN_BB = Comparator.comparing(SessionHand::netProfitInBb);

	public SessionHandStats {
		points = List.copyOf(points);
		resultsByPosition = List.copyOf(resultsByPosition);
	}

	public record Point(String handId, Instant time, BigDecimal netProfit, BigDecimal netProfitInBb,
			BigDecimal cumulativeNetProfit, BigDecimal cumulativeNetProfitInBb, BigDecimal startingStack,
			BigDecimal stackInBb) {

	}

	public record PositionResult(Position position, int hands, BigDecimal netProfit, BigDecimal netProfitInBb) {

	}

	static SessionHandStats of(List<SessionHand> hands) {
		if (hands.isEmpty()) {
			throw new IllegalArgumentException("A session has at least one hand");
		}

		List<Point> points = new ArrayList<>(hands.size());
		BigDecimal profit = ZERO;
		BigDecimal profitInBb = ZERO;
		int vpipCount = 0;
		int pfrCount = 0;

		for (SessionHand hand : hands) {
			profit = profit.add(hand.netProfit());
			profitInBb = profitInBb.add(hand.netProfitInBb());
			points.add(new Point(hand.handId(), hand.time(), hand.netProfit(), hand.netProfitInBb(), profit,
					profitInBb, hand.startingStack(), hand.startingStack().divide(hand.blindValue(), 2, HALF_UP)));
			vpipCount += hand.vpip() ? 1 : 0;
			pfrCount += hand.pfr() ? 1 : 0;
		}

		int handCount = hands.size();
		return new SessionHandStats(points, percentage(vpipCount, handCount), percentage(pfrCount, handCount),
				profitInBb.multiply(HUNDRED).divide(BigDecimal.valueOf(handCount), 2, HALF_UP),
				hands.stream().max(BY_NET_PROFIT_IN_BB).orElseThrow(),
				hands.stream().min(BY_NET_PROFIT_IN_BB).orElseThrow(), byPosition(hands));
	}

	private static List<PositionResult> byPosition(List<SessionHand> hands) {
		Map<Position, PositionResult> results = new EnumMap<>(Position.class);

		for (SessionHand hand : hands) {
			PositionResult current = results.getOrDefault(hand.position(),
					new PositionResult(hand.position(), 0, ZERO, ZERO));
			results.put(hand.position(), new PositionResult(hand.position(), current.hands() + 1,
					current.netProfit().add(hand.netProfit()), current.netProfitInBb().add(hand.netProfitInBb())));
		}

		return List.copyOf(results.values());
	}

	private static BigDecimal percentage(int count, int total) {
		return BigDecimal.valueOf(count).multiply(HUNDRED).divide(BigDecimal.valueOf(total), 2, HALF_UP);
	}

}
