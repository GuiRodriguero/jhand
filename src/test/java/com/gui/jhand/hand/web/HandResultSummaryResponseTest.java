package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

class HandResultSummaryResponseTest {

    private HandResult entity;

    @BeforeEach
    void setUp() {
        entity = valid(HandResult.class);
    }

    @Test
    void should_instantiate() {
        assertThat(HandResultSummaryResponse.of(entity)).isEqualTo(new HandResultSummaryResponse(entity.getHandId(), entity.getSessionId(), entity.getHeroCards().toString(),
                entity.getHandRank().name(), entity.getNetProfit(), ofInstant(entity.getTime(), systemDefault())));
    }

}
