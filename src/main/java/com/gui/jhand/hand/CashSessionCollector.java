package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.pokersession.CashPokerSession;
import com.gui.jhand.pokersession.PokerSession;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.math.BigDecimal.ZERO;

class CashSessionCollector implements SessionCollector {

	private BigDecimal netProfit = ZERO;

	@Override
	public void accept(List<Action> actions, HandResult hand) {
		netProfit = netProfit.add(hand.getNetProfit());
	}

	@Override
	public PokerSession toSession(String sessionId, int handCount, Instant startTime) {
		return CashPokerSession.of(sessionId, handCount, startTime, netProfit);
	}

}
