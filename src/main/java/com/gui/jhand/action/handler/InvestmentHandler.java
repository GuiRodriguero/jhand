package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static com.gui.jhand.action.ActionType.*;

@Component
public class InvestmentHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(POST_BLIND, ACTION_CALL, ACTION_BET);
	}

	@Override
	public void handle(Action action, HandState state) {
		if (state.getHeroName().equals(action.getPlayerName())) {
			state.addTotalInvested(action.getAmount());
			state.addCurrentStreetInvestment(action.getAmount());

			if (state.getCurrentStreet() == PRE_FLOP && action.getType() == ACTION_CALL) {
				state.setVpip(true);
			}
		}
	}

}