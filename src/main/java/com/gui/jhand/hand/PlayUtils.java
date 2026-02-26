package com.gui.jhand.hand;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gui.jhand.hand.Action.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PlayUtils {

	private static final Pattern ACTION_PATTERN = Pattern.compile(
			"^(raises|calls|posts|bets|folds|checks)(?:.*? to \\$?([\\d.]+)|[^\\d]+([\\d.]+))?",
			Pattern.CASE_INSENSITIVE);

	static List<Play> getPlaysOnHand(String hand, String playerName) {
		List<Play> plays = new ArrayList<>();

		double currentStreetInvested = 0.0;
		String heroPrefix = playerName + ": ";

		for (String line : hand.split("\\r?\\n")) {

			if (line.startsWith("*** FLOP ***") || line.startsWith("*** TURN ***")
					|| line.startsWith("*** RIVER ***")) {
				currentStreetInvested = 0.0;
				continue;
			}

			if (!line.startsWith(playerName + ":")) {
				continue;
			}

			Matcher matcher = ACTION_PATTERN.matcher(line.substring(heroPrefix.length()));
			if (matcher.find()) {
				String actionStr = matcher.group(1).toUpperCase();

				if (actionStr.equals(FOLDS.name()) || actionStr.equals(CHECKS.name())) {
					plays.add(Play.of(playerName, Action.valueOf(actionStr), 0.0));
					continue;
				}

				double amount = 0.0;
				if (matcher.group(2) != null) {
					amount = getRaisesToAmount(matcher);
				}
				else if (matcher.group(3) != null) {
					amount = getCallsPostsAndBetsAmount(matcher);
				}

				if (actionStr.equals(BETS.name()) || actionStr.equals(CALLS.name())) {
					currentStreetInvested += amount;
					plays.add(Play.of(playerName, Action.valueOf(actionStr), amount));
				}
				else if (actionStr.equals(RAISES.name())) {
					double addedChips = amount - currentStreetInvested;
					currentStreetInvested = amount;
					plays.add(Play.of(playerName, RAISES, addedChips));
				}
				else {
					plays.add(Play.of(playerName, POSTS, amount));
				}
			}
		}

		return plays;
	}

	public static double getAmountInvestedForLine(String line, String playerName, double currentStreetInvested) {
		String heroPrefix = playerName + ": ";

		Matcher matcher = ACTION_PATTERN.matcher(line.substring(heroPrefix.length()));
		if (matcher.find()) {
			String actionStr = matcher.group(1).toUpperCase();

			if (actionStr.equals(FOLDS.name()) || actionStr.equals(CHECKS.name())) {
				return 0.0;
			}

			double amount = 0.0;
			if (matcher.group(2) != null) {
				amount = getRaisesToAmount(matcher);
			}
			else if (matcher.group(3) != null) {
				amount = getCallsPostsAndBetsAmount(matcher);
			}

			if (actionStr.equals(BETS.name()) || actionStr.equals(CALLS.name())) {
				return amount;
			}
			else if (actionStr.equals(RAISES.name())) {
				return amount - currentStreetInvested;
			}
			else {
				return amount;
			}
		}

		return 0.0;
	}

	private static double getRaisesToAmount(Matcher matcher) {
		return Double.parseDouble(matcher.group(2));
	}

	private static double getCallsPostsAndBetsAmount(Matcher matcher) {
		return Double.parseDouble(matcher.group(3));
	}

}
