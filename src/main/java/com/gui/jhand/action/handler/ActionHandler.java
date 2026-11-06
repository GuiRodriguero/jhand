package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;

import java.util.List;

public interface ActionHandler {

	List<ActionType> getSupportedTypes();

	void handle(Action action, HandState state);

}