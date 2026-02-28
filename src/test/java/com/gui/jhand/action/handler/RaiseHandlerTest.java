package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.ACTION_RAISE;
import static org.assertj.core.api.Assertions.assertThat;

class RaiseHandlerTest {

    private RaiseHandler handler;

    private Action action;

    private HandState state;

    @BeforeEach
    void setUp() {
        handler = new RaiseHandler();
        action = Instancio.of(Action.class).create();
        state = Instancio.of(HandState.class).create();
    }

    @Test
    void should_get_supported_types() {
        assertThat(handler.getSupportedTypes()).containsExactly(ACTION_RAISE);
    }

    @Test
    void should_handle() {
        action = ActionTemplateLoader.validGui();
        state = HandStateTemplateLoader.validGui();

    }

    @Test
    void should_handle_pfr() {
        action = ActionTemplateLoader.validGui();
        state = HandStateTemplateLoader.validGuiPreFlop();

    }

    @Test
    void should_not_handle_when_player_is_different() {
        state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

    }

}
