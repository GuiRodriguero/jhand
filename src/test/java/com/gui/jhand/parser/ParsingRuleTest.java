package com.gui.jhand.parser;

import com.gui.jhand.action.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gui.jhand.action.ActionType.BOARD;
import static org.assertj.core.api.Assertions.assertThat;

class ParsingRuleTest {

	private ParsingRule rule;

	private Pattern pattern;

	private ActionMapper mapper;

	private ParserContext context;

	@BeforeEach
	void setUp() {
		context = new ParserContext();
		pattern = Pattern.compile("^Board \\[(.*?)]");
		mapper = (m, ctx, line) -> List.of(Action.of(BOARD, ctx.getCurrentStreet(), null, m.group(1), 0, line));
		rule = new ParsingRule(pattern, mapper);
	}

	@Test
	void should_try_match() {
		String line = "Board [Qs Jd 9c 6h 9h]";

		Matcher matcher = pattern.matcher(line);
		matcher.find();

		assertThat(rule.tryMatch(line, context)).isEqualTo(mapper.map(matcher, context, line));
	}

	@Test
	void should_return_empty_if_did_not_match() {
		assertThat(rule.tryMatch("GuiRodri2013: shows [2h Ac] (a pair of Nines)", context)).isEmpty();
	}

}
