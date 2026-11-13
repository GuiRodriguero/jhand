package com.gui.jhand.hand.stats;

import com.gui.jhand.hand.HandResult;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class SessionHandTest {

	@Test
	void should_instantiate_from_hand_result() {
		HandResult hand = valid(HandResult.class);

		assertThat(SessionHand.of(hand)).isEqualTo(new SessionHand(hand.getHandId(), hand.getTime(),
				hand.getHeroCards(), hand.getPosition(), hand.getStartingStack(), hand.getBlindValue(), hand.isVpip(),
				hand.isPfr(), hand.getNetProfit(), hand.getNetProfitInBb(), hand.getHandRank()));
	}

}
