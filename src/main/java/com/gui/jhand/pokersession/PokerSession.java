package com.gui.jhand.pokersession;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

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

	private Integer handCount;

	private Instant startTime;

	@Column(precision = 12, scale = 2)
	private BigDecimal netProfit;

}
