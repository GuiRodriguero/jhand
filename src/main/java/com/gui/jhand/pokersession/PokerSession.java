package com.gui.jhand.pokersession;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.DiscriminatorType.STRING;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Getter
@EqualsAndHashCode
@Inheritance(strategy = SINGLE_TABLE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@DiscriminatorColumn(name = "type", discriminatorType = STRING)
public abstract class PokerSession {

	@Id
	private String sessionId;

	@Enumerated(EnumType.STRING)
	@Column(insertable = false, updatable = false)
	private SessionType type;

	private Integer handCount;

	private Instant startTime;

	@Column(precision = 12, scale = 2)
	private BigDecimal netProfit;

}
