package com.gui.jhand.parser;

import com.gui.jhand.action.Action;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
class ParsingRule {

	private final Pattern pattern;

	private final ActionMapper mapper;

	List<Action> tryMatch(String line, ParserContext context) {
		Matcher matcher = pattern.matcher(line);

		return matcher.find() ? mapper.map(matcher, context, line) : List.of();
	}

}
