package com.gui.jhand.parser;

import com.gui.jhand.action.Action;

import java.util.List;
import java.util.regex.Matcher;

@FunctionalInterface
interface ActionMapper {

	List<Action> map(Matcher matcher, ParserContext context, String rawLine);

}
