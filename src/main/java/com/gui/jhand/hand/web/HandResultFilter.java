package com.gui.jhand.hand.web;

import com.gui.jhand.pokersession.SessionType;

public record HandResultFilter(String handId, String sessionId, SessionType sessionType, String heroCards,
		String handRank) {
}
