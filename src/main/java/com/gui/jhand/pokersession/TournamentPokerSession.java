package com.gui.jhand.pokersession;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DiscriminatorValue("TOURNAMENT")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class TournamentPokerSession extends PokerSession {

	@Column(precision = 12, scale = 2)
	private BigDecimal buyIn;

	@Column(precision = 12, scale = 2)
	private BigDecimal prize;

	private Integer finishPosition;

	private Boolean realMoney;

	private TournamentPokerSession(String sessionId, Integer handCount, Instant startTime, Instant endTime,
			TournamentResult result) {
		super(sessionId, TOURNAMENT, handCount, startTime, endTime, result.netProfit());
		this.buyIn = result.buyIn();
		this.prize = result.finished() ? result.prize() : null;
		this.finishPosition = result.finishPosition();
		this.realMoney = result.realMoney();
	}

	public static TournamentPokerSession of(String sessionId, Integer handCount, Instant startTime, Instant endTime,
			TournamentResult result) {
		return new TournamentPokerSession(sessionId, handCount, startTime, endTime, result);
	}

}
