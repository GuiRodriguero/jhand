package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.handler.ActionHandler;
import com.gui.jhand.action.handler.ActionHandlerRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gui.jhand.hand.PositionUtils.resolvePosition;

@Service
@AllArgsConstructor
class HandResultBuilder {

	private final ActionHandlerRegistry handlers;

	HandResult build(List<Action> actions, String heroName) {
		HandState state = new HandState(heroName);

		for (Action action : actions) {
			if (action.getStreet() != state.getCurrentStreet()) {
				state.updateCurrentStreet(action.getStreet());
			}

			ActionHandler handler = handlers.getHandler(action.getType());
			if (handler != null) {
				handler.handle(action, state);
			}
		}

		return HandResult.builder()
			.handId(state.getHandId())
			.time(state.getTime())
			.heroCards(state.getHeroCards())
			.position(resolvePosition(state))
				.blindValue(state.getBlindValue())
			.vpip(state.isVpip())
			.pfr(state.isPfr())
			.totalInvested(state.getTotalInvested())
			.totalCollected(state.getTotalCollected())
			.netProfit(state.getNetProfit())
			.build();
	}

}
