package com.gui.jhand.core.utils;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor(access = PRIVATE)
public final class ChipsUtils {

	public static BigDecimal of(String amount) {
		return hasText(amount) ? new BigDecimal(amount).setScale(2, HALF_UP) : ZERO.setScale(2, HALF_UP);
	}

	public static BigDecimal of(double amount) {
		return BigDecimal.valueOf(amount).setScale(2, HALF_UP);
	}

}
