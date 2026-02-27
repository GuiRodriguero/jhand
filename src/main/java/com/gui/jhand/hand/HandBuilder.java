package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionStreet;

import java.util.List;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static com.gui.jhand.action.ActionType.*;
import static com.gui.jhand.hand.Position.*;

class HandBuilder {

	HandResult build(List<Action> actions, String heroName) {
		String handId = null;
		String heroCards = null;
		boolean vpip = false;
		boolean pfr = false;

		double totalInvested = 0.0;
		double currentStreetInvestment = 0.0;
		double totalCollected = 0.0;

		ActionStreet currentStreet = PRE_FLOP;

		for (Action action : actions) {

			if (action.getStreet() != currentStreet) {
				currentStreetInvestment = 0.0;
				currentStreet = action.getStreet();
			}

			if (action.getType() == HEADER) {
				handId = action.getMainInformation();
			}

			if (heroName.equals(action.getPlayerName())) {

				switch (action.getType()) {
					case DEALT_TO_HERO:
						heroCards = action.getMainInformation();
						break;

					case POST_ANTE:
						totalInvested += action.getAmount();
						break;

					case POST_BLIND:
					case ACTION_CALL:
					case ACTION_BET:
						totalInvested += action.getAmount();
						currentStreetInvestment += action.getAmount();

						if (currentStreet == PRE_FLOP && action.getType() == ACTION_CALL) {
							vpip = true;
						}
						break;

					case ACTION_RAISE:
						double actualCost = action.getAmount() - currentStreetInvestment;
						totalInvested += actualCost;
						currentStreetInvestment = action.getAmount();

						if (currentStreet == PRE_FLOP) {
							vpip = true;
							pfr = true;
						}
						break;

					case UNCALLED_BET_RETURNED:
						totalInvested -= action.getAmount();
						break;

					case COLLECTED_POT:
						totalCollected += action.getAmount();
						break;

					default:
						break;
				}
			}
		}

		return HandResult.builder()
			.handId(handId)
			.heroCards(heroCards)
			.position(calculatePosition(actions, heroName))
			.vpip(vpip)
			.pfr(pfr)
			.totalInvested(totalInvested)
			.totalCollected(totalCollected)
			.netProfit(totalCollected - totalInvested)
			.build();
	}

	private Position calculatePosition(List<Action> actions, String heroName) {
		List<Action> seats = actions.stream().filter(a -> a.getType() == SEAT_HAND_RESULT).toList();

		int btnIndex = -1;
		int heroIndex = -1;
		Action heroAction = null;

		for (int i = 0; i < seats.size(); i++) {
			Action seatAction = seats.get(i);
			String rawLine = seatAction.getRawLine().toLowerCase();

			if (rawLine.contains("(button)")) {
				btnIndex = i;
			}

			if (heroName.equals(seatAction.getPlayerName())) {
				heroIndex = i;
				heroAction = seatAction;
			}
		}

		if (heroAction != null) {
			String heroRaw = heroAction.getRawLine().toLowerCase();
			if (heroRaw.contains("(button)")) {
				return BTN;
			}
			if (heroRaw.contains("(small blind)")) {
				return SB;
			}
			if (heroRaw.contains("(big blind)")) {
				return BB;
			}

			int activePlayersSize = seats.size();
			int distanceFromBtn = (heroIndex - btnIndex + activePlayersSize) % activePlayersSize;

			if (distanceFromBtn == 3) {
				return UTG;
			}
			else if (distanceFromBtn == activePlayersSize - 1) {
				return CO;
			}
		}

		return MP;
	}

}
