package com.gui.jhand.parser;

import com.gui.jhand.action.Action;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.gui.jhand.action.ActionStreet.*;
import static com.gui.jhand.action.ActionType.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class PokerStarsParsingRuleFactory {

	static List<ParsingRule> create() {
		List<ParsingRule> rules = new ArrayList<>();

		rules.add(new ParsingRule(Pattern.compile("^PokerStars Hand #(\\d+): "),
				(m, ctx, line) -> Action.of(HEADER, ctx.getCurrentStreet(), null, m.group(1), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^Table '([^']+)'"),
				(m, ctx, line) -> Action.of(TABLE_INFO, ctx.getCurrentStreet(), null, m.group(1), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^Seat (\\d+): (.*?) \\(.*?in chips.*?\\)"),
				(m, ctx, line) -> Action.of(SEAT_INFO, ctx.getCurrentStreet(), m.group(2), m.group(1), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): posts the ante (\\d+)"),
				(m, ctx, line) -> Action.of(POST_ANTE, ctx.getCurrentStreet(), m.group(1),
						"posts the ante " + m.group(2), Double.parseDouble(m.group(2)), line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): posts (?:small|big) blind (\\d+)"),
				(m, ctx, line) -> Action.of(POST_BLIND, ctx.getCurrentStreet(), m.group(1), "posts blind " + m.group(2),
						Double.parseDouble(m.group(2)), line)));

		rules.add(new ParsingRule(Pattern.compile("^Dealt to (.*?) \\[(.*?)]"),
				(m, ctx, line) -> Action.of(DEALT_TO_HERO, ctx.getCurrentStreet(), m.group(1), m.group(2), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): calls (\\d+)(?: and is all-in)?"),
				(m, ctx, line) -> Action.of(ACTION_CALL, ctx.getCurrentStreet(), m.group(1), "calls " + m.group(2),
						Double.parseDouble(m.group(2)), line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): raises (\\d+) to (\\d+)"),
				(m, ctx, line) -> Action.of(ACTION_RAISE, ctx.getCurrentStreet(), m.group(1),
						"raises " + m.group(2) + " to " + m.group(3), Double.parseDouble(m.group(3)), line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): bets \\$?([\\d.]+)(?: and is all-in)?"),
				(m, ctx, line) -> Action.of(ACTION_BET, ctx.getCurrentStreet(), m.group(1), "bets " + m.group(2),
						Double.parseDouble(m.group(2)), line)));

		rules.add(new ParsingRule(Pattern.compile("^\\*\\*\\* FLOP \\*\\*\\* \\[(.*?)]"), (m, ctx, line) -> {
			ctx.setCurrentStreet(FLOP);
			return Action.of(FLOP_CARDS, ctx.getCurrentStreet(), null, m.group(1), 0, line);
		}));

		rules.add(new ParsingRule(Pattern.compile("^\\*\\*\\* TURN \\*\\*\\* \\[.*?] \\[(.*?)]"), (m, ctx, line) -> {
			ctx.setCurrentStreet(TURN);
			return Action.of(TURN_CARDS, ctx.getCurrentStreet(), null, m.group(1), 0, line);
		}));

		rules.add(new ParsingRule(Pattern.compile("^\\*\\*\\* RIVER \\*\\*\\* \\[.*?] \\[(.*?)]"), (m, ctx, line) -> {
			ctx.setCurrentStreet(RIVER);
			return Action.of(RIVER_CARDS, ctx.getCurrentStreet(), null, m.group(1), 0, line);
		}));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): shows \\[(.*?)] (.*)"), (m, ctx, line) -> Action
			.of(CARDS_SHOWN, ctx.getCurrentStreet(), m.group(1), "shows [" + m.group(2) + "] " + m.group(3), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^(.*?) collected (\\d+) from (?:(.*) )?pot"), (m, ctx, line) -> {
			String potType = m.group(3) == null ? "" : m.group(3) + " ";
			return Action.of(COLLECTED_POT, ctx.getCurrentStreet(), m.group(1),
					"collected " + m.group(2) + " from " + potType + "pot", Double.parseDouble(m.group(2)), line);
		}));

		rules.add(new ParsingRule(Pattern.compile("^\\*\\*\\* SUMMARY \\*\\*\\*"), (m, ctx, line) -> {
			ctx.setCurrentStreet(SUMMARY);
			return Action.of(SUMMARY_HEADER, ctx.getCurrentStreet(), null, null, 0, line);
		}));

		rules.add(new ParsingRule(Pattern.compile("^(.*?): folds"),
				(m, ctx, line) -> Action.of(ACTION_FOLD, ctx.getCurrentStreet(), m.group(1), "folds", 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^Uncalled bet \\((\\d+)\\) returned to (.*)"),
				(m, ctx, line) -> Action.of(UNCALLED_BET_RETURNED, ctx.getCurrentStreet(), m.group(2), m.group(1),
						Double.parseDouble(m.group(1)), line)));

		rules.add(new ParsingRule(Pattern.compile("^Total pot (\\d+)(?: \\| Rake (\\d+))?"),
				(m, ctx, line) -> Action.of(TOTAL_POT, ctx.getCurrentStreet(), null, "Total pot " + m.group(1),
						Double.parseDouble(m.group(1)), line)));

		rules.add(new ParsingRule(Pattern.compile("^Board \\[(.*?)]"),
				(m, ctx, line) -> Action.of(BOARD, ctx.getCurrentStreet(), null, m.group(1), 0, line)));

		rules.add(new ParsingRule(Pattern.compile("^Seat (\\d+): (.*?) (?:\\((.*?)\\) )?(.*)"), (m, ctx, line) -> Action
			.of(SEAT_HAND_RESULT, ctx.getCurrentStreet(), m.group(2), m.group(4), 0, line)));

		return rules;
	}

}
