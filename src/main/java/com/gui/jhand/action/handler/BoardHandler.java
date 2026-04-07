package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.BOARD;
import static com.gui.jhand.core.poker.Board.fromString;

@Component
@EqualsAndHashCode
public class BoardHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(BOARD);
	}

	@Override
	public void handle(Action action, HandState state) {
		state.setBoard(fromString(action.getMainInformation()));
	}

}
