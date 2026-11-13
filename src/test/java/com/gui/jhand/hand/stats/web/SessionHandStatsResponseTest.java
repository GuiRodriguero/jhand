package com.gui.jhand.hand.stats.web;

import com.gui.jhand.hand.stats.SessionHand;
import com.gui.jhand.hand.stats.SessionHandStats;
import com.gui.jhand.hand.stats.web.SessionHandStatsResponse.HandResponse;
import com.gui.jhand.hand.stats.web.SessionHandStatsResponse.PointResponse;
import com.gui.jhand.hand.stats.web.SessionHandStatsResponse.PositionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class SessionHandStatsResponseTest {

	@Test
	void should_instantiate() {
		SessionHandStats stats = valid(SessionHandStats.class);
		SessionHandStats.Point point = stats.points().getFirst();
		SessionHandStats.PositionResult position = stats.resultsByPosition().getFirst();
		SessionHand best = stats.bestHand();
		SessionHand worst = stats.worstHand();

		SessionHandStatsResponse response = SessionHandStatsResponse.of(stats);

		assertThat(response.points()).hasSize(stats.points().size())
			.first()
			.isEqualTo(new PointResponse(point.handId(), point.time(), point.netProfit(), point.netProfitInBb(),
					point.cumulativeNetProfit(), point.cumulativeNetProfitInBb(), point.startingStack(),
					point.stackInBb()));
		assertThat(response.byPosition()).hasSize(stats.resultsByPosition().size())
			.first()
			.isEqualTo(new PositionResponse(position.position().name(), position.hands(), position.netProfit(),
					position.netProfitInBb()));
		assertThat(response.bestHand())
			.isEqualTo(new HandResponse(best.handId(), best.time(), best.heroCards().toString(), best.position().name(),
					best.handRank().name(), best.netProfit(), best.netProfitInBb()));
		assertThat(response.worstHand())
			.isEqualTo(new HandResponse(worst.handId(), worst.time(), worst.heroCards().toString(),
					worst.position().name(), worst.handRank().name(), worst.netProfit(), worst.netProfitInBb()));
		assertThat(List.of(response.vpipPercentage(), response.pfrPercentage(), response.bbPer100()))
			.containsExactly(stats.vpipPercentage(), stats.pfrPercentage(), stats.bbPer100());
	}

}
