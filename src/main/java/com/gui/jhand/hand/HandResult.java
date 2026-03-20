package com.gui.jhand.hand;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

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

	private Instant time;

	private String heroCards;

	private Position position;

	private double blindValue;

	private boolean vpip;

	private boolean pfr;

	private double totalInvested;

	private double totalCollected;

	private double netProfit;

}
