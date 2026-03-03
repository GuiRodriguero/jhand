package com.gui.jhand.parser;

import com.gui.jhand.action.ActionStreet;
import lombok.Getter;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;

@Getter
class ParserContext {

	private ActionStreet currentStreet = PRE_FLOP;

	void changeCurrentStreetTo(ActionStreet street) {
		currentStreet = street;
	}

}
