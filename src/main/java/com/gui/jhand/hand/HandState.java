package com.gui.jhand.hand;

import com.gui.jhand.action.ActionStreet;
import lombok.Data;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;

@Data
public class HandState {

	private final String heroName;

	private String handId;

	private String heroCards;

	private boolean vpip;

	private boolean pfr;

	private double totalInvested;

	private double currentStreetInvestment;

	private double totalCollected;

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
		this.currentStreetInvestment = 0.0;
	}

	public void addTotalInvested(double amount) {
		this.totalInvested += amount;
	}

	public void addCurrentStreetInvestment(double amount) {
		this.currentStreetInvestment += amount;
	}

	public void addTotalCollected(double amount) {
		this.totalCollected += amount;
	}

	public void subtractTotalInvested(double amount) {
		this.totalInvested -= amount;
	}

}