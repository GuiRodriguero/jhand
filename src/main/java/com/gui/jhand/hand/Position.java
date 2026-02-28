package com.gui.jhand.hand;

import org.jspecify.annotations.Nullable;

public enum Position {

	BTN, SB, BB, CO, MP, UTG;

	@Nullable
	public static Position getExplicitPosition(String rawAction) {
		if (rawAction.contains("(button)")) {
			return BTN;
		}
		if (rawAction.contains("(small blind)")) {
			return SB;
		}
		if (rawAction.contains("(big blind)")) {
			return BB;
		}

		return null;
	}

}
