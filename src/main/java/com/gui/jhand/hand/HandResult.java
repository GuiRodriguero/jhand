package com.gui.jhand.hand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of", access = PACKAGE)
class HandResult {

	private String handId;

	private Instant time;

	private String heroCards;

	private Position position;

	private double blindValue;

	private boolean vpip;

	private boolean pfr;

	private double totalInvested;

	private double totalCollected;

	private double netProfit;

}
