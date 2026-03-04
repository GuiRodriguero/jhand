package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.gui.jhand.action.ActionType.TIME;

@Component
public class TimeHandler implements ActionHandler {

	private static final ZoneId ET_ZONE = ZoneId.of("America/New_York");

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	@Override
	public List<ActionType> getSupportedTypes() {
		return List.of(TIME);
	}

	@Override
	public void handle(Action action, HandState state) {
		String etTimeString = action.getMainInformation();
		LocalDateTime localDateTime = LocalDateTime.parse(etTimeString, FORMATTER);
		Instant utcInstant = ZonedDateTime.of(localDateTime, ET_ZONE).toInstant();

		state.setTime(utcInstant);
	}

}
