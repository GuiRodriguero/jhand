package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.COLLECTED_POT;

@Component
public class CollectedHandler implements ActionHandler {

    @Override
    public List<ActionType> getSupportedTypes() {
        return List.of(COLLECTED_POT);
    }

    @Override
    public void handle(Action action, HandState state) {
        if (state.getHeroName().equals(action.getPlayerName())) {
            state.addTotalCollected(action.getAmount());
        }
    }

}
