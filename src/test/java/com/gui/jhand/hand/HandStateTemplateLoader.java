package com.gui.jhand.hand;

import org.instancio.Instancio;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static org.instancio.Select.field;

public class HandStateTemplateLoader {

    public static HandState validGui() {
        return Instancio.of(HandState.class).set(field(HandState::getHeroName), "GuiRodri2013").create();
    }

    public static HandState validHeroDifferentFromAction(String actionPlayerName) {
        return Instancio.of(HandState.class).set(field(HandState::getHeroName), actionPlayerName.concat("different")).create();
    }

    public static HandState validGuiPreFlop() {
        return Instancio.of(HandState.class)
                .set(field(HandState::getHeroName), "GuiRodri2013")
                .set(field(HandState::getCurrentStreet), PRE_FLOP)
                .create();
    }

}
