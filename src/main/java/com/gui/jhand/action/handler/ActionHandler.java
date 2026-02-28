package com.gui.jhand.action.handler;

import com.gui.jhand.action.Action;
import com.gui.jhand.action.ActionType;
import com.gui.jhand.hand.HandState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ActionHandler {

    List<ActionType> getSupportedTypes();

    void handle(Action action, HandState state);

}