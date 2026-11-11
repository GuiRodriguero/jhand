package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.TOTAL_POT;

@Component
public class TotalPotHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(TOTAL_POT);
	}

	@Override
	public void handle(Action action, HandState state) {
		state.setTotalPot(action.getAmount());
	}

}
