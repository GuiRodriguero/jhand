package com.gui.jhand.pokersession;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static lombok.AccessLevel.PROTECTED;

@Entity
@DiscriminatorValue("CASH_GAME")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class CashPokerSession extends PokerSession {

	private CashPokerSession(String sessionId, Integer handCount, Instant startTime, BigDecimal handsNetProfit) {
		super(sessionId, CASH_GAME, handCount, startTime, handsNetProfit);
	}

	public static CashPokerSession of(String sessionId, Integer handCount, Instant startTime,
			BigDecimal handsNetProfit) {
		return new CashPokerSession(sessionId, handCount, startTime, handsNetProfit);
	}

}
