package com.gui.jhand.parser;

import com.gui.jhand.action.Action;

import java.util.regex.Matcher;

@FunctionalInterface
interface ActionMapper {

	Action map(Matcher matcher, ParserContext context, String rawLine);

}
