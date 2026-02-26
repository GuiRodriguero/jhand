package com.gui.jhand.hand;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gui.jhand.hand.PlayUtils.getPlaysOnHand;
import static com.gui.jhand.hand.Position.*;
import static java.util.regex.Pattern.DOTALL;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class HandUtils {

	private static final Pattern HAND_ID_PATTERN = Pattern.compile("Hand #(\\d+):");

	private static final Pattern PRE_FLOP_PATTERN = Pattern.compile(
			"\\*\\*\\* HOLE CARDS \\*\\*\\*(.*?)(?=\\*\\*\\* FLOP \\*\\*\\*|\\*\\*\\* SUMMARY \\*\\*\\*)",
			Pattern.DOTALL);

	private static final Pattern SUMMARY_PATTERN = Pattern.compile("\\*\\*\\* SUMMARY \\*\\*\\*(.*)", DOTALL);

	private static final Pattern ACTIVE_PLAYER_PATTERN = Pattern.compile("Seat \\d+: ([^($\\n]+?)(?:[ ($]|$)");

	static String getHandId(String hand) {
		String handId = "";

		Matcher idMatcher = HAND_ID_PATTERN.matcher(hand);
		if (idMatcher.find()) {
			handId = idMatcher.group(1);
		}

		return handId;
	}

	static String getHeroCards(String hand, String heroName) {// TODO: Return List<Card>
																// or similar
		String heroCards = "";

		Matcher cardsMatcher = Pattern.compile("Dealt to " + heroName + " \\[(.*?)]").matcher(hand);
		if (cardsMatcher.find()) {
			heroCards = cardsMatcher.group(1);
		}

		return heroCards;
	}

	static boolean isVpip(String hand, String playerName) {
		String preFlopActions = getPreFlopActions(hand);

		return preFlopActions.contains(playerName + ": raises") || preFlopActions.contains(playerName + ": calls");
	}

	static boolean isPfr(String hand, String playerName) {
		return getPreFlopActions(hand).contains(playerName + ": raises");
	}

	private static String getPreFlopActions(String hand) {
		Matcher matcher = PRE_FLOP_PATTERN.matcher(hand);
		return matcher.find() ? matcher.group(1) : "";
	}

	static Position getPosition(String hand, String playerName) {
		String summary = getSummary(hand);
		if (summary.contains(playerName + " (button)")) {
			return BTN;
		}
		else if (summary.contains(playerName + " (small blind)")) {
			return SB;
		}
		else if (summary.contains(playerName + " (big blind)")) {
			return BB;
		}

		List<String> activePlayers = getActivePlayers(summary);
		int distanceFromBtn = getDistanceFromBtn(summary, playerName, activePlayers);

		if (distanceFromBtn == 3) {
			return UTG;
		}
		else if (distanceFromBtn == activePlayers.size() - 1) {
			return CO;
		}

		return MP;
	}

	private static String getSummary(String hand) {
		Matcher summaryMatcher = SUMMARY_PATTERN.matcher(hand);
		return summaryMatcher.find() ? summaryMatcher.group(1) : "";
	}

	private static List<String> getActivePlayers(String summary) {
		List<String> activePlayers = new ArrayList<>();

		Matcher seatMatcher = ACTIVE_PLAYER_PATTERN.matcher(summary);
		while (seatMatcher.find()) {
			String name = seatMatcher.group(1).trim();
			if (!activePlayers.contains(name)) {
				activePlayers.add(name);
			}
		}

		return activePlayers;
	}

	private static int getDistanceFromBtn(String summary, String playerName, List<String> activePlayers) {
		int activePlayersSize = activePlayers.size();

		int btnIndex = -1;
		int playerIndex = -1;
		for (int i = 0; i < activePlayersSize; i++) {
			if (summary.contains(activePlayers.get(i) + " (button)")) {
				btnIndex = i;
			}

			if (activePlayers.get(i).equals(playerName)) {
				playerIndex = i;
			}
		}

		return (playerIndex - btnIndex + activePlayersSize) % activePlayersSize;
	}

	static double getNetProfit(String hand, String heroName) {
		return getCollectedBet(hand, heroName) - getInvestedNet(hand, heroName);
	}

	static double getCollectedBet(String hand, String heroName) {
		String summary = getSummary(hand);
		String safeHeroName = Pattern.quote(heroName);

		Matcher showdownMatcher = Pattern.compile(safeHeroName + ".*showed.*\\[.*].*won \\(\\$?([\\d.]+)\\)")
			.matcher(summary);
		if (showdownMatcher.find()) {
			return Double.parseDouble(showdownMatcher.group(1));
		}

		Matcher collectedMatcher = Pattern.compile(safeHeroName + ".*collected \\(\\$?([\\d.]+)\\)").matcher(summary);
		if (collectedMatcher.find()) {
			return Double.parseDouble(collectedMatcher.group(1));
		}

		return 0.0;
	}

	static double getInvestedNet(String hand, String heroName) {
		double invested = getPlaysOnHand(hand, heroName).stream().mapToDouble(Play::getAmount).sum();

		String safeHeroName = Pattern.quote(heroName);
		Matcher returnedMatcher = Pattern.compile("Uncalled bet \\(\\$?([\\d.]+)\\) returned to " + safeHeroName)
			.matcher(hand);
		if (returnedMatcher.find()) {
			invested -= Double.parseDouble(returnedMatcher.group(1));
		}

		return invested;
	}

}
