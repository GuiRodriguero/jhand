package com.gui.jhand.action;

import org.instancio.Instancio;

import static com.gui.jhand.action.ActionType.*;
import static org.instancio.Select.field;

public class ActionTemplateLoader {

	public static Action validGui() {
		return Instancio.of(Action.class).set(field(Action::getPlayerName), "GuiRodri2013").create();
	}

	public static Action validGuiCall() {
		return Instancio.of(Action.class)
			.set(field(Action::getPlayerName), "GuiRodri2013")
			.set(field(Action::getType), ACTION_CALL)
			.create();
	}

	public static Action validGuiPostBlind() {
		return Instancio.of(Action.class)
			.set(field(Action::getPlayerName), "GuiRodri2013")
			.set(field(Action::getType), POST_BLIND)
			.create();
	}

	public static Action validGuiRaise() {
		return Instancio.of(Action.class)
			.set(field(Action::getPlayerName), "GuiRodri2013")
			.set(field(Action::getType), ACTION_RAISE)
			.create();
	}

	public static Action validGuiSeatResultAtBB() {
		return Instancio.of(Action.class)
			.set(field(Action::getPlayerName), "GuiRodri2013")
			.set(field(Action::getType), SEAT_HAND_RESULT)
			.set(field(Action::getRawLine), "GuiRodri2013 (big blind) folded on the Flop")
			.create();
	}

	public static Action validGuiSeatResultAtBtn() {
		return Instancio.of(Action.class)
			.set(field(Action::getPlayerName), "GuiRodri2013")
			.set(field(Action::getType), SEAT_HAND_RESULT)
			.set(field(Action::getRawLine), "GuiRodri2013 (button) folded on the Flop")
			.create();
	}

}
