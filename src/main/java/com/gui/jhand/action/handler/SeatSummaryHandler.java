package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionType.SEAT_HAND_RESULT;

@Component
public class SeatSummaryHandler implements ActionHandler {

    @Override
    public List<ActionType> getSupportedTypes() {
        return List.of(SEAT_HAND_RESULT);
    }

    @Override
    public void handle(Action action, HandState state) {
        int currentIndex = state.getActivePlayersCount();
        String rawLine = action.getRawLine().toLowerCase();

        if (rawLine.contains("(button)")) {
            state.setBtnIndex(currentIndex);
        }

        if (state.getHeroName().equals(action.getPlayerName())) {
            state.setHeroIndex(currentIndex);
            state.setHeroSummaryLine(rawLine);
        }

        state.setActivePlayersCount(currentIndex + 1);
    }

}
