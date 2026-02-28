package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.POST_ANTE;

@Component
public class PostAnteHandler implements ActionHandler {

    @Override
    public List<ActionType> getSupportedTypes() {
        return List.of(POST_ANTE);
    }

    @Override
    public void handle(Action action, HandState state) {
        if (action.getPlayerName().equals(state.getHeroName())) {
            state.addTotalInvested(action.getAmount());
        }
    }

}
