package com.gui.jhand.parser;

import com.gui.jhand.action.Action;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
class ParsingRule {

	private final Pattern pattern;

	private final ActionMapper mapper;

	Optional<Action> tryMatch(String line, ParserContext ctx) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			return Optional.of(mapper.map(matcher, ctx, line));
		}
		return Optional.empty();
	}

}
