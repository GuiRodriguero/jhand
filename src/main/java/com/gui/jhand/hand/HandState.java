package com.gui.jhand.hand;

import com.gui.jhand.action.ActionStreet;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static java.math.BigDecimal.ZERO;

@Data
public class HandState {

	private final String heroName;

	private String handId;

	private Instant time;

	private String heroCards;

	private BigDecimal blindValue = ZERO;

	private boolean vpip;

	private boolean pfr;

	private BigDecimal totalInvested = ZERO;

	private BigDecimal currentStreetInvestment = ZERO;

	private BigDecimal totalCollected = ZERO;

	private int activePlayersCount;

	private int btnIndex = -1;

	private int heroIndex = -1;

	private String heroSummaryLine;

	private ActionStreet currentStreet = PRE_FLOP;

	public HandState(String heroName) {
		this.heroName = heroName;
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

}