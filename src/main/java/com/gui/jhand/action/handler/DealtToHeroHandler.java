package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.DEALT_TO_HERO;

@Component
@EqualsAndHashCode
public class DealtToHeroHandler implements ActionHandler {

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(DEALT_TO_HERO);
	}

	@Override
	public void handle(Action action, HandState state) {
		if (state.getHeroName().equals(action.getPlayerName())) {
			state.setHeroCards(action.getMainInformation());
		}
	}

}
