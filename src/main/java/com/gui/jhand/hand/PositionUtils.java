package com.gui.jhand.hand;

import lombok.NoArgsConstructor;

import static com.gui.jhand.hand.Position.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PositionUtils {

	public static Position resolvePosition(HandState state) {
		return getExplicitPosition(state.getHeroSummaryLine()).orElseGet(() -> {
			int count = state.getActivePlayersCount();
			int distanceFromBtn = (state.getHeroIndex() - state.getBtnIndex() + count) % count;

			if (distanceFromBtn == 3) {
				return UTG;
			}

			if (distanceFromBtn == count - 1) {
				return CO;
			}

			return MP;
		});
	}

}
