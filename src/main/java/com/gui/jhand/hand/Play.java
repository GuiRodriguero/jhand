package com.gui.jhand.hand;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
class Play {

	private String player;

	private Action action;

	private double amount;

}
