package com.gui.jhand.hand;

import lombok.NoArgsConstructor;

import static com.gui.jhand.hand.Position.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PositionUtils {

	public static Position resolvePosition(HandState state) {
		Position position = getExplicitPosition(state.getHeroSummaryLine());
		if (position != null) {
			return position;
		}

		int count = state.getActivePlayersCount();
		int distanceFromBtn = (state.getHeroIndex() - state.getBtnIndex() + count) % count;

		if (distanceFromBtn == 3) {
			return UTG;
		}
		else if (distanceFromBtn == count - 1) {
			return CO;
		}

		return MP;
	}

}
