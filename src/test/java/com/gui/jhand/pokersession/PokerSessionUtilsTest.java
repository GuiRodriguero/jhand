package com.gui.jhand.pokersession;

import com.gui.jhand.TestBase;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.pokersession.PokerSessionUtils.normalizeSessionId;
import static org.assertj.core.api.Assertions.assertThat;

class PokerSessionUtilsTest extends TestBase {

	@Override
	public void init() {

	}

	@Test
	void should_normalize_session_id() {
		assertThat(normalizeSessionId("My Session.txt")).isEqualTo("My Session");
	}

	@Test
	void should_normalize_session_id_without_extension() {
		assertThat(normalizeSessionId("My Session")).isEqualTo("My Session");
	}

}
