package com.gui.jhand.parser;

import com.gui.jhand.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokerStarsParser {

	private final List<ParsingRule> rules = PokerStarsParsingRuleFactory.create();

	public List<Action> parse(String rawHand) {
		List<Action> actions = new ArrayList<>();
		ParserContext context = new ParserContext();

		for (String line : rawHand.split("\\r?\\n")) {
			line = line.trim();
			for (ParsingRule rule : rules) {
				Optional<Action> action = rule.tryMatch(line, context);
				if (action.isPresent()) {
					actions.add(action.get());
					break;
				}
			}
		}

		return actions;
	}

}
