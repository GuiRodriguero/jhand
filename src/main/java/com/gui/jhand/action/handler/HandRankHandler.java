package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.HAND_RANK;
import static com.gui.jhand.core.poker.HandRank.fromRawHand;

@Component
@EqualsAndHashCode
public class HandRankHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(HAND_RANK);
	}

	@Override
	public void handle(Action action, HandState state) {
		state.setHandRank(fromRawHand(action.getMainInformation()));
	}

}
