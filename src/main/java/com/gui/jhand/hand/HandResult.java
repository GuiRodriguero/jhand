package com.gui.jhand.hand;

import com.gui.jhand.core.poker.HandRank;
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
class HandResult {

	@Id
	private String handId;

	private String sessionId;

	private Instant time;

	private String heroCards;

	@Enumerated(value = STRING)
	private Position position;

	@Column(precision = 12, scale = 2)
	private BigDecimal blindValue;

	private boolean vpip;

	private boolean pfr;

	@Column(precision = 12, scale = 2)
	private BigDecimal totalInvested;

	@Column(precision = 12, scale = 2)
	private BigDecimal totalCollected;

	@Column(precision = 12, scale = 2)
	private BigDecimal netProfit;

	@Enumerated(value = STRING)
	private HandRank handRank;

}
