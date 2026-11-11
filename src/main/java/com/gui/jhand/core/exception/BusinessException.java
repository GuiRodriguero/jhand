package com.gui.jhand.core.exception;

/**
 * Signals that a poker business rule was violated (e.g. a hand without a big blind). Its
 * message is safe to expose to the client.
 */
public class BusinessException extends RuntimeException {

	public BusinessException(String message) {
		super(message);
	}

}
