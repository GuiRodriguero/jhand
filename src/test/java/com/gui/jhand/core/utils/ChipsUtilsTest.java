package com.gui.jhand.core.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;

class ChipsUtilsTest {

	@Test
	void should_generate_big_decimal_from_string() {
		assertThat(ChipsUtils.of("800")).isEqualTo(new BigDecimal("800").setScale(2, HALF_UP));
	}

	@Test
	void should_generate_big_decimal_from_empty_string() {
		assertThat(ChipsUtils.of(" ")).isEqualTo(ZERO.setScale(2, HALF_UP));
	}

	@Test
	void should_generate_big_decimal_from_null_string() {
		assertThat(ChipsUtils.of(null)).isEqualTo(ZERO.setScale(2, HALF_UP));
	}

	@Test
	void should_generate_big_decimal_from_double() {
		assertThat(ChipsUtils.of(800.0)).isEqualTo(BigDecimal.valueOf(800.0).setScale(2, HALF_UP));
	}

}
