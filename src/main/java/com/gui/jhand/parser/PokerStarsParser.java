package com.gui.jhand.parser;

import com.gui.jhand.hand.Hand;
import com.gui.jhand.hand.HandStreet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gui.jhand.hand.Action.CHECKS;
import static com.gui.jhand.hand.Action.FOLDS;
import static com.gui.jhand.hand.HandStreet.*;
import static com.gui.jhand.hand.PlayUtils.getAmountInvestedForLine;

public class PokerStarsParser {

	private static final Pattern HAND_ID_PATTERN = Pattern.compile("Hand #(\\d+):");

	private static final Pattern WON_PATTERN = Pattern.compile("won \\(\\$?([\\d.]+)\\)");

	private static final Pattern COLLECTED_PATTERN = Pattern.compile("collected \\(\\$?([\\d.]+)\\)");

	private static final Pattern UNCALLED_PATTERN = Pattern.compile("Uncalled bet \\(\\$?([\\d.]+)\\)");

	public Hand parse(String rawHand, String heroName) {
		String handId = "";
		String heroCards = "";
		boolean isVpip = false;
		boolean isPfr = false;
		double collectedBet = 0.0;
		double investedNet = 0.0;
		double currentStreetInvested = 0.0;
		HandStreet street = PRE_FLOP;

		String heroPrefix = heroName + ": ";
		String heroRaises = heroPrefix + "raises";
		String heroCalls = heroPrefix + "calls";

		String[] lines = rawHand.split("\\r?\\n");

		for (String line : lines) {

			HandStreet newStreet = getCurrentHandStreet(line, street);
			if (newStreet != street) {
				street = newStreet;
				currentStreetInvested = 0.0;
			}

			if (handId.isEmpty()) {
				handId = getHandId(line);
			}

			if (heroCards.isEmpty()) {
				heroCards = getHeroCards(line, heroName);
			}

			if (isHeroAction(line, heroPrefix)) {
				double addedChips = getAmountInvestedForLine(line, heroName, currentStreetInvested);
				investedNet += addedChips;

				if (!line.contains("ante") && !line.contains(FOLDS.name()) && !line.contains(CHECKS.name())) {
					currentStreetInvested += addedChips;
				}

				if (street == PRE_FLOP) {
					if (!isPfr && line.contains(heroRaises)) {
						isPfr = isPfr(line, heroName);
					}
					if (!isVpip && (isPfr || line.contains(heroCalls))) {
						isVpip = isVpip(line, heroName);
					}
				}
			}

			if (street == SUMMARY) {
				if (line.contains(heroName)) {
					collectedBet += getCollectedBet(line);
				}
			}

			if (line.startsWith("Uncalled bet") && line.contains(heroName)) {
				Matcher uncalledBetMatcher = UNCALLED_PATTERN.matcher(line);
				if (uncalledBetMatcher.find()) {
					investedNet -= Double.parseDouble(uncalledBetMatcher.group(1));
				}
			}

		}

		return Hand.of(handId, heroCards, isVpip, isPfr, collectedBet - investedNet);
	}

	private HandStreet getCurrentHandStreet(String line, HandStreet currentPhase) {
		if (line.startsWith("*** FLOP")) {
			return FLOP;
		}
		else if (line.startsWith("*** TURN")) {
			return TURN;
		}
		else if (line.startsWith("*** RIVER")) {
			return RIVER;
		}
		else if (line.startsWith("*** SUMMARY")) {
			return SUMMARY;
		}

		return currentPhase;
	}

	private String getHandId(String line) {
		String handId = "";

		if (line.startsWith("PokerStars Hand #")) {
			Matcher matcher = HAND_ID_PATTERN.matcher(line);
			if (matcher.find()) {
				handId = matcher.group(1);
			}
		}

		return handId;
	}

	private String getHeroCards(String line, String heroName) {
		String heroCards = "";

		String prefix = "Dealt to " + heroName;
		if (line.startsWith(prefix)) {
			heroCards = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		}

		return heroCards;
	}

	private boolean isHeroAction(String line, String heroPrefix) {
		return line.startsWith(heroPrefix);
	}

	private boolean isVpip(String line, String playerName) {
		return isPfr(line, playerName) || line.contains(playerName + ": calls");
	}

	private boolean isPfr(String line, String playerName) {
		return line.contains(playerName + ": raises");
	}

	private double getCollectedBet(String line) {
		double collected = 0.0;

		Matcher showdownMatcher = WON_PATTERN.matcher(line);
		if (showdownMatcher.find()) {
			return Double.parseDouble(showdownMatcher.group(1));
		}

		Matcher collectedMatcher = COLLECTED_PATTERN.matcher(line);
		if (collectedMatcher.find()) {
			return Double.parseDouble(collectedMatcher.group(1));
		}

		return collected;
	}

}
