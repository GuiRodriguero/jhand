package com.gui.jhand.pokersession;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DiscriminatorValue("CASH_GAME")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class CashPokerSession extends PokerSession {

	@Column(precision = 12, scale = 2)
	private BigDecimal bigBlind;

	private CashPokerSession(String sessionId, Integer handCount, Instant startTime, Instant endTime,
			BigDecimal handsNetProfit, BigDecimal bigBlind) {
		super(sessionId, CASH_GAME, handCount, startTime, endTime, handsNetProfit);
		this.bigBlind = bigBlind;
	}

	public static CashPokerSession of(String sessionId, Integer handCount, Instant startTime, Instant endTime,
			BigDecimal handsNetProfit, BigDecimal bigBlind) {
		return new CashPokerSession(sessionId, handCount, startTime, endTime, handsNetProfit, bigBlind);
	}

}
