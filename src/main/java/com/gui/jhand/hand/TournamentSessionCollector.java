package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;
import com.gui.jhand.pokersession.TournamentResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.gui.jhand.action.ActionType.TOURNAMENT_BUY_IN;
import static com.gui.jhand.action.ActionType.TOURNAMENT_FINISH;
import static java.math.BigDecimal.ZERO;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
class TournamentSessionCollector implements SessionCollector {

	private String heroName;

	private BigDecimal buyIn;

	private Action heroFinish;

	private boolean realMoney;

	@Override
	public void accept(List<Action> actions, HandResult hand) {
		for (Action action : actions) {
			if (buyIn == null && action.getType() == TOURNAMENT_BUY_IN) {
				buyIn = action.getAmount();
				realMoney |= action.getMainInformation().contains("$");
			}
			else if (action.getType() == TOURNAMENT_FINISH && heroName.equals(action.getPlayerName())) {
				heroFinish = action;
				realMoney |= action.getRawLine().contains("$");
			}
		}
	}

	@Override
	public PokerSession toSession(String sessionId, int handCount, Instant startTime, Instant endTime) {
		return TournamentPokerSession.of(sessionId, handCount, startTime, endTime, toResult());
	}

	TournamentResult toResult() {
		BigDecimal resolvedBuyIn = buyIn != null ? buyIn : ZERO;

		if (heroFinish == null) {
			return new TournamentResult(resolvedBuyIn, ZERO, null, realMoney, false);
		}

		String position = heroFinish.getMainInformation();
		return new TournamentResult(resolvedBuyIn, heroFinish.getAmount(),
				position != null ? Integer.valueOf(position) : null, realMoney, true);
	}

	static TournamentSessionCollector of(String heroName) {
		return new TournamentSessionCollector(heroName, null, null, false);
	}

}
