package com.gui.jhand.evaluator;

import com.gui.jhand.core.poker.Card;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class HandEvaluatorUtils {

    // 2 to ACE
    private static final int[] PRIMES = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };

    static int convertCard(Card card) {
        int rank = card.rank().getValue();
        int suit = card.suit().getValue();
        int prime = PRIMES[rank];

        // 32 bits:
        // bit 16-28: rank bitmask (1 << (rank + 16))
        // bit 8-11: rank (0 - 12)
        // bit 12-15: suit (1, 2, 4, 8)
        // bit 0-7: prime number
        return (1 << (16 + rank)) | (rank << 8) | (suit << 12) | prime;
    }

}
