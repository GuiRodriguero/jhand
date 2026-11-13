package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.HandRank;
import com.gui.jhand.core.poker.PocketCards;
import com.gui.jhand.pokersession.SessionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(staticName = "of", access = PACKAGE)
@Table(indexes = @Index(name = "idx_hand_result_session_id", columnList = "sessionId"))
public class HandResult {

	@Id
	private String handId;

	private String sessionId;

	@Enumerated(value = STRING)
	private SessionType sessionType;

	private Instant time;

	@Convert(converter = PocketCardsConverter.class)
	private PocketCards heroCards;

	@Enumerated(value = STRING)
	private Position position;

	@Column(precision = 12, scale = 2)
	private BigDecimal blindValue;

	@Column(precision = 12, scale = 2)
	private BigDecimal startingStack;

	private boolean vpip;

	private boolean pfr;

	@Column(precision = 12, scale = 2)
	private BigDecimal totalInvested;

	@Column(precision = 12, scale = 2)
	private BigDecimal totalCollected;

	@Column(precision = 12, scale = 2)
	private BigDecimal netProfit;

	@Column(precision = 12, scale = 2)
	private BigDecimal netProfitInBb;

	@Column(precision = 12, scale = 2)
	private BigDecimal totalPot;

	@Enumerated(value = STRING)
	private HandRank handRank;

	@Convert(converter = BoardConverter.class)
	private Board board;

}
