package com.gui.jhand.hand;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class Hand {

	private String handId;

	private String heroCards;

	private boolean vpip;

	private boolean pfr;

	public static Hand of(String hand, String heroName) {
		return Hand.of(HandUtils.getHandId(hand), HandUtils.getHeroCards(hand, heroName),
				HandUtils.isVpip(hand, heroName), HandUtils.isPfr(hand, heroName));
	}

}
