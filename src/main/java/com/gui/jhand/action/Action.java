package com.gui.jhand.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Action {

	private ActionType type;

	private ActionStreet street;

	private String playerName;

	private String mainInformation;

	private double amount;

	private String rawLine;

}
