package com.gui.jhand.evaluator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SenzeeHashTest {

	@Test
	void should_find_correct_score_for_known_product() {
		assertThat(SenzeeHash.find(41 * 41 * 41 * 41 * 37)).isEqualTo(11);
	}

	@Test
	void should_find_correct_score_for_four_deuces_trey_kicker() {
		assertThat(SenzeeHash.find(2 * 2 * 2 * 2 * 3)).isEqualTo(166);
	}

}
