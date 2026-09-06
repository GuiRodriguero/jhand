package com.gui.jhand.hand;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public enum Position {

	BTN, SB, BB, CO, MP, UTG;

	public static Optional<Position> getExplicitPosition(String rawAction) {
		if (rawAction.contains("(button)")) {
			return of(BTN);
		}
		if (rawAction.contains("(small blind)")) {
			return of(SB);
		}
		if (rawAction.contains("(big blind)")) {
			return of(BB);
		}

		return empty();
	}

}
