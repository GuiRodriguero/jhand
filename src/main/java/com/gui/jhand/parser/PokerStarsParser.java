package com.gui.jhand.parser;

import com.gui.jhand.action.Action;

import java.util.ArrayList;
import java.util.List;

public class PokerStarsParser {

	private final List<ParsingRule> rules = PokerStarsParsingRuleFactory.create();

	public List<Action> parse(String rawHand) {
		List<Action> actions = new ArrayList<>();
		ParserContext context = new ParserContext();

		for (String line : rawHand.split("\\r?\\n")) {
			line = line.trim();
			for (ParsingRule rule : rules) {
				List<Action> action = rule.tryMatch(line, context);
				if (!action.isEmpty()) {
					actions.addAll(action);
					break;
				}
			}
		}

		return actions;
	}

}
