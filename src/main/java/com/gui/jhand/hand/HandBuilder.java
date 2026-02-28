package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.action.handler.ActionHandler;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.gui.jhand.hand.PositionUtils.resolvePosition;

@Service
class HandBuilder {

    private final Map<ActionType, ActionHandler> handlers = new EnumMap<>(ActionType.class);

    public HandBuilder(List<ActionHandler> availableHandlers) {
        for (ActionHandler handler : availableHandlers) {
            for (ActionType type : handler.getSupportedTypes()) {
                handlers.put(type, handler);
            }
        }
    }

    HandResult build(List<Action> actions, String heroName) {
        HandState state = new HandState(heroName);

        for (Action action : actions) {
            if (action.getStreet() != state.getCurrentStreet()) {
                state.updateCurrentStreet(action.getStreet());
            }

            ActionHandler handler = handlers.get(action.getType());
            if (handler != null) {
                handler.handle(action, state);
            }
        }

        return HandResult.builder()
                .handId(state.getHandId())
                .heroCards(state.getHeroCards())
                .position(resolvePosition(state))
                .vpip(state.isVpip())
                .pfr(state.isPfr())
                .totalInvested(state.getTotalInvested())
                .totalCollected(state.getTotalCollected())
                .netProfit(state.getTotalCollected() - state.getTotalInvested())
                .build();
    }

}
