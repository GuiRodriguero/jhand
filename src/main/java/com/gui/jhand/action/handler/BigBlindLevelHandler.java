package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.BIG_BLIND_LEVEL;

@Component
public class BigBlindLevelHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(BIG_BLIND_LEVEL);
	}

	@Override
	public void handle(Action action, HandState state) {
		state.setBlindValue(action.getAmount());
	}

}
