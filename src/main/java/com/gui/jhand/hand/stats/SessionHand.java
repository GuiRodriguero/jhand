package com.gui.jhand.hand.stats;

import com.gui.jhand.core.poker.HandRank;
import com.gui.jhand.core.poker.PocketCards;
import com.gui.jhand.hand.HandResult;
import com.gui.jhand.hand.Position;

import java.math.BigDecimal;
import java.time.Instant;

public record SessionHand(String handId, Instant time, PocketCards heroCards, Position position,
		BigDecimal startingStack, BigDecimal blindValue, boolean vpip, boolean pfr, BigDecimal netProfit,
		BigDecimal netProfitInBb, HandRank handRank) {

	static SessionHand of(HandResult hand) {
		return new SessionHand(hand.getHandId(), hand.getTime(), hand.getHeroCards(), hand.getPosition(),
				hand.getStartingStack(), hand.getBlindValue(), hand.isVpip(), hand.isPfr(), hand.getNetProfit(),
				hand.getNetProfitInBb(), hand.getHandRank());
	}

}
