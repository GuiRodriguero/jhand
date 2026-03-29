package com.gui.jhand.core.utils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class ChipsUtils {

	public static BigDecimal of(String amount) {
		if (amount == null || amount.isBlank()) {
			return ZERO.setScale(2, HALF_UP);
		}

		return new BigDecimal(amount).setScale(2, HALF_UP);
	}

	public static BigDecimal of(double amount) {
		return BigDecimal.valueOf(amount).setScale(2, HALF_UP);
	}

}
