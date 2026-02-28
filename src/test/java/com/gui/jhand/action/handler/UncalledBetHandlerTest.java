package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.UNCALLED_BET_RETURNED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class UncalledBetHandlerTest {

    private UncalledBetHandler handler;

    private Action action;

    private HandState state;

    @BeforeEach
    void setUp() {
        handler = new UncalledBetHandler();
        action = Instancio.of(Action.class).create();
        state = Instancio.of(HandState.class).create();
    }

    @Test
    void should_get_supported_types() {
        assertThat(handler.getSupportedTypes()).containsExactly(UNCALLED_BET_RETURNED);
    }

    @Test
    void should_handle() {
        action = ActionTemplateLoader.validGui();
        state = HandStateTemplateLoader.validGui();

        double totalInvestedBeforeHandle = state.getTotalInvested();

        assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
        assertThat(state.getTotalInvested()).isEqualTo(totalInvestedBeforeHandle - action.getAmount());
    }

    @Test
    void should_not_handle_when_player_is_different() {
        state = HandStateTemplateLoader.validHeroDifferentFromAction(action.getPlayerName());

        double totalInvestedBeforeHandle = state.getTotalCollected();

        assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
        assertThat(state.getTotalCollected()).isEqualTo(totalInvestedBeforeHandle);
    }

}
