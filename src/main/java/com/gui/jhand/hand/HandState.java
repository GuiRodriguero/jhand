package com.gui.jhand.hand;

import com.gui.jhand.action.ActionStreet;
import com.gui.jhand.core.exception.BusinessException;
import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@Data
public class HandState {

	private final String heroName;

	private String handId;

	private final String sessionId;

	private Instant time;

	private PocketCards heroCards;

	private BigDecimal blindValue = ZERO;

	private BigDecimal startingStack;

	private boolean vpip;

	private boolean pfr;

	private BigDecimal totalInvested = ZERO;

	private BigDecimal currentStreetInvestment = ZERO;

	private BigDecimal totalCollected = ZERO;

	private BigDecimal totalPot = ZERO;

	private int activePlayersCount;

	private int btnIndex = -1;

	private int heroIndex = -1;

	private String heroSummaryLine;

	private ActionStreet currentStreet = PRE_FLOP;

	private Board board;

	public HandState(String heroName, String sessionId) {
		this.heroName = heroName;
		this.sessionId = sessionId;
	}

	public void updateCurrentStreet(ActionStreet street) {
		this.setCurrentStreet(street);
		this.currentStreetInvestment = ZERO;
	}

	public void addTotalInvested(BigDecimal amount) {
		this.totalInvested = this.totalInvested.add(amount);
	}

	public void addCurrentStreetInvestment(BigDecimal amount) {
		this.currentStreetInvestment = this.currentStreetInvestment.add(amount);
	}

	public void addTotalCollected(BigDecimal amount) {
		this.totalCollected = this.totalCollected.add(amount);
	}

	public void subtractTotalInvested(BigDecimal amount) {
		this.totalInvested = this.totalInvested.subtract(amount);
	}

	public BigDecimal getNetProfit() {
		return this.totalCollected.subtract(this.totalInvested);
	}

	public BigDecimal getNetProfitInBb() {
		if (this.blindValue == null || this.blindValue.signum() <= 0) {
			throw new BusinessException(
					"Hand %s has no valid big blind value: %s".formatted(this.handId, this.blindValue));
		}
		return getNetProfit().divide(this.blindValue, 2, HALF_UP);
	}

}