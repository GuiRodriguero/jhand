package com.gui.jhand.pokersession;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

import static com.gui.jhand.pokersession.SessionType.fromFileName;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Table
@Entity
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(staticName = "of", access = PACKAGE)
public class PokerSession {

	@Id
	private String sessionId;

	@Enumerated(STRING)
	private SessionType type;

	private BigDecimal netProfit;

	static PokerSession of(String fileName, BigDecimal netProfit) {
		return PokerSession.of(fileName, fromFileName(fileName), netProfit);
	}

}
