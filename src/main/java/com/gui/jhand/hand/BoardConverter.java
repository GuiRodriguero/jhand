package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Board;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.gui.jhand.core.poker.Board.fromString;
import static org.springframework.util.StringUtils.hasText;

@Converter(autoApply = true)
class BoardConverter implements AttributeConverter<Board, String> {

	@Override
	public String convertToDatabaseColumn(Board attribute) {
		return attribute != null ? attribute.toString() : null;
	}

	@Override
	public Board convertToEntityAttribute(String dbData) {
		return hasText(dbData) ? fromString(dbData) : null;
	}

}
