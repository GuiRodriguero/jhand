package com.gui.jhand.hand;

import com.gui.jhand.core.utils.ChipsUtils;
import org.instancio.Instancio;

import java.time.Instant;

import static org.instancio.Select.field;

class HandResultTemplateLoader {

	static HandResult handWithNetProfitAt(String netProfit, Instant time) {
		return Instancio.of(HandResult.class)
			.set(field(HandResult::getNetProfit), ChipsUtils.of(netProfit))
			.set(field(HandResult::getTime), time)
			.create();
	}

}
