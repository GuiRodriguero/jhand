package com.gui.jhand.parser;

import com.gui.jhand.action.ActionStreet;
import lombok.Getter;
import lombok.Setter;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;

@Getter
@Setter
class ParserContext {

	private ActionStreet currentStreet = PRE_FLOP;

}
