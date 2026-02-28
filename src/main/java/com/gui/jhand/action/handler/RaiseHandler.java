package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.action.ActionStreet.PRE_FLOP;
import static com.gui.jhand.action.ActionType.ACTION_RAISE;

@Component
public class RaiseHandler implements ActionHandler {

    @Override
    public List<ActionType> getSupportedTypes() {
        return List.of(ACTION_RAISE);
    }

    @Override
    public void handle(Action action, HandState state) {
        if (state.getHeroName().equals(action.getPlayerName())) {
            double actualCost = action.getAmount() - state.getCurrentStreetInvestment();
            state.addTotalInvested(actualCost);
            state.setCurrentStreetInvestment(action.getAmount());

            if (state.getCurrentStreet() == PRE_FLOP) {
                state.setVpip(true);
                state.setPfr(true);
            }
        }
    }
}