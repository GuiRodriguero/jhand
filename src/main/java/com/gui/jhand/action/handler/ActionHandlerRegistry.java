package com.gui.jhand.action.handler;

import com.gui.jhand.action.ActionType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ActionHandlerRegistry {

	private final Map<ActionType, ActionHandler> handlers = new EnumMap<>(ActionType.class);

	public ActionHandlerRegistry(List<ActionHandler> availableHandlers) {
		availableHandlers.forEach(handler -> handler.getSupportedTypes().forEach(type -> handlers.put(type, handler)));
	}

	public ActionHandler getHandler(ActionType type) {
		return handlers.get(type);
	}

}
