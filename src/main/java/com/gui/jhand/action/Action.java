package com.gui.jhand.action;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class Action {

	private ActionType type;

	private ActionStreet street;

	private String playerName;

	private String mainInformation;

	private BigDecimal amount;

	private String rawLine;

}
