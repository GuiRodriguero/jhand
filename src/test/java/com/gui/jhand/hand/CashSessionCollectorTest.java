package com.gui.jhand.hand;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.pokersession.CashPokerSession;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static com.gui.jhand.hand.HandResultTemplateLoader.handWithNetProfitAt;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class CashSessionCollectorTest extends TestBase {

	private static final Instant START_TIME = Instant.ofEpochSecond(1771774381);

	private CashSessionCollector collector;

	@Override
	public void init() {
		collector = new CashSessionCollector();
	}

	@Test
	void should_build_cash_session_with_the_sum_of_hands_net_profit() {
		collector.accept(List.of(), handWithNetProfitAt("150.25", START_TIME));
		collector.accept(List.of(), handWithNetProfitAt("-50.10", START_TIME.plusSeconds(60)));

		assertThat(collector.toSession("My Cash", 2, START_TIME))
			.isEqualTo(CashPokerSession.of("My Cash", 2, START_TIME, ChipsUtils.of("100.15")));
	}

	@Test
	void should_build_cash_session_with_zero_net_profit_without_hands() {
		assertThat(collector.toSession("My Cash", 0, null).getNetProfit()).isEqualTo(ZERO);
	}

}
