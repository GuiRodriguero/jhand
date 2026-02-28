package com.gui.jhand.action;

import org.instancio.Instancio;

import static com.gui.jhand.action.ActionType.ACTION_CALL;
import static com.gui.jhand.action.ActionType.POST_BLIND;
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

}
