package com.gui.jhand.hand;

import com.gui.jhand.core.poker.StartingHand;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static org.springframework.util.StringUtils.hasText;

@Converter(autoApply = true)
public class StartingHandConverter implements AttributeConverter<StartingHand, String> {

	@Override
	public String convertToDatabaseColumn(StartingHand attribute) {
		return attribute != null ? attribute.toString() : null;
	}

	@Override
	public StartingHand convertToEntityAttribute(String dbData) {
		return hasText(dbData) ? StartingHand.fromDb(dbData) : null;
	}

}