package com.gui.jhand.hand;

import com.gui.jhand.core.utils.ChipsUtils;
import org.instancio.Instancio;

import static org.instancio.Select.field;

class HandResultTemplateLoader {

	static HandResult handWithNetProfit(String netProfit) {
		return Instancio.of(HandResult.class).set(field(HandResult::getNetProfit), ChipsUtils.of(netProfit)).create();
	}

}
