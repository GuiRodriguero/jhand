package com.gui.jhand.hand.stats;

import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.hand.Position;
import org.instancio.Instancio;

import static org.instancio.Select.field;

class SessionHandTemplateLoader {

	static SessionHand sessionHand(String handId, Position position, String startingStack, boolean vpip, boolean pfr,
			String netProfit, String netProfitInBb) {
		return Instancio.of(SessionHand.class)
			.set(field(SessionHand::handId), handId)
			.set(field(SessionHand::position), position)
			.set(field(SessionHand::startingStack), ChipsUtils.of(startingStack))
			.set(field(SessionHand::blindValue), ChipsUtils.of("0.02"))
			.set(field(SessionHand::vpip), vpip)
			.set(field(SessionHand::pfr), pfr)
			.set(field(SessionHand::netProfit), ChipsUtils.of(netProfit))
			.set(field(SessionHand::netProfitInBb), ChipsUtils.of(netProfitInBb))
			.create();
	}

}
