package com.gui.jhand.hand;

import com.gui.jhand.core.poker.PocketCards;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.gui.jhand.core.poker.PocketCards.fromDb;
import static org.springframework.util.StringUtils.hasText;

@Converter(autoApply = true)
public class PocketCardsConverter implements AttributeConverter<PocketCards, String> {

	@Override
	public String convertToDatabaseColumn(PocketCards attribute) {
		return attribute != null ? attribute.toString() : null;
	}

	@Override
	public PocketCards convertToEntityAttribute(String dbData) {
		return hasText(dbData) ? fromDb(dbData) : null;
	}

}