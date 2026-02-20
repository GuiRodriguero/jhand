package com.gui.jhand.hand;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class HandUtils {

	static String getHandId(String hand) {
		String handId = "";

		Matcher idMatcher = Pattern.compile("Hand #(\\d+):").matcher(hand);
		if (idMatcher.find()) {
			handId = idMatcher.group(1);
		}

		return handId;
	}

	static String getHeroCards(String hand, String heroName) {
		String heroCards = "";

		Matcher cardsMatcher = Pattern.compile("Dealt to " + heroName + " \\[(.*?)]").matcher(hand);
		if (cardsMatcher.find()) {
			heroCards = cardsMatcher.group(1);
		}

		return heroCards;
	}

	static boolean isVpip(String hand, String heroName) {
		String preFlopActions = getPreFlopActions(hand);

		return preFlopActions.contains(heroName + ": raises") || preFlopActions.contains(heroName + ": calls");
	}

	static boolean isPfr(String hand, String heroName) {
		String preFlopActions = getPreFlopActions(hand);

		return preFlopActions.contains(heroName + ": raises");
	}

	private static String getPreFlopActions(String hand) {
		String preFlopActions = "";
		Pattern preFlopPattern = Pattern.compile(
				"\\*\\*\\* HOLE CARDS \\*\\*\\*(.*?)(?:\\*\\*\\* FLOP \\*\\*\\*|\\*\\*\\* SUMMARY \\*\\*\\*)", DOTALL);
		Matcher preFlopMatcher = preFlopPattern.matcher(hand);

		if (preFlopMatcher.find()) {
			preFlopActions = preFlopMatcher.group(1);
		}

		return preFlopActions;
	}

}
