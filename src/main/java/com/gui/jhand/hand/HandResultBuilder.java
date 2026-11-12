package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.handler.ActionHandler;
import com.gui.jhand.action.handler.ActionHandlerRegistry;
import com.gui.jhand.pokersession.SessionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gui.jhand.hand.PositionUtils.resolvePosition;

@Service
@AllArgsConstructor
class HandResultBuilder {

	private final ActionHandlerRegistry handlers;

	private final HandRankResolver handRankResolver;

	HandResult build(List<Action> actions, String sessionId, SessionType sessionType, String heroName) {
		HandState state = new HandState(heroName, sessionId);

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
			.sessionId(state.getSessionId())
			.sessionType(sessionType)
			.time(state.getTime())
			.heroCards(state.getHeroCards())
			.position(resolvePosition(state))
			.blindValue(state.getBlindValue())
			.startingStack(state.getStartingStack())
			.vpip(state.isVpip())
			.pfr(state.isPfr())
			.totalInvested(state.getTotalInvested())
			.totalCollected(state.getTotalCollected())
			.netProfit(state.getNetProfit())
			.netProfitInBb(state.getNetProfitInBb())
			.totalPot(state.getTotalPot())
			.handRank(handRankResolver.calculatedRank(state.getHeroCards(), state.getBoard()))
			.board(state.getBoard())
			.build();
	}

}
