package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionTemplateLoader;
import com.gui.jhand.hand.HandState;
import com.gui.jhand.hand.HandStateTemplateLoader;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.gui.jhand.action.ActionType.TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TimeHandlerTest {

	private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	private TimeHandler handler;

	private Action action;

	private HandState state;

	@BeforeEach
	void setUp() {
		handler = new TimeHandler();
		action = Instancio.of(Action.class).create();
		state = Instancio.of(HandState.class).create();
	}

	@Test
	void should_get_supported_types() {
		assertThat(handler.getSupportedTypes()).containsExactly(TIME);
	}

	@Test
	void should_handle() {
		action = ActionTemplateLoader.validNow();
		state = HandStateTemplateLoader.validGui();

		assertThatCode(() -> handler.handle(action, state)).doesNotThrowAnyException();
		assertThat(state.getTime()).isEqualTo(getUtcInstant(action.getMainInformation()));
	}

	private Instant getUtcInstant(String time) {
		LocalDateTime localDateTime = LocalDateTime.parse(time, FORMATTER);
		return ZonedDateTime.of(localDateTime, ET_ZONE).toInstant();
	}

}
