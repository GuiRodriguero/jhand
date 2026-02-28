package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.action.ActionType.HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class HeaderHandlerTest {

    private HeaderHandler handler;

    private Action action;

    private HandState state;

    @BeforeEach
    void setUp() {
        handler = new HeaderHandler();
        action = Instancio.of(Action.class).create();
        state = Instancio.of(HandState.class).create();
    }

    @Test
    void should_get_supported_types() {
        assertThat(handler.getSupportedTypes()).containsExactly(HEADER);
    }

    @Test
    void should_handle() {
        action = ActionTemplateLoader.validGui();
        state = HandStateTemplateLoader.validGui();

        assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
        assertThat(state.getHandId()).isEqualTo(action.getMainInformation());
    }

}
