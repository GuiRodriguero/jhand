package com.gui.jhand.evaluator;

import static com.gui.jhand.evaluator.SenzeeHash.find;
import static java.lang.Integer.MAX_VALUE;

class HandEvaluator {

    private static final int[][] SEVEN_CARDS_COMBINATION = {
            {0, 1, 2, 3, 4}, {0, 1, 2, 3, 5}, {0, 1, 2, 3, 6}, {0, 1, 2, 4, 5},
            {0, 1, 2, 4, 6}, {0, 1, 2, 5, 6}, {0, 1, 3, 4, 5}, {0, 1, 3, 4, 6},
            {0, 1, 3, 5, 6}, {0, 1, 4, 5, 6}, {0, 2, 3, 4, 5}, {0, 2, 3, 4, 6},
            {0, 2, 3, 5, 6}, {0, 2, 4, 5, 6}, {0, 3, 4, 5, 6}, {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 6}, {1, 2, 3, 5, 6}, {1, 2, 4, 5, 6}, {1, 3, 4, 5, 6},
            {2, 3, 4, 5, 6}
    };

    private static final short[] FLUSH_TABLE = new short[8192];

    private static final short[] UNIQUE_FIVE_CARD_COMBINATION = new short[4887];

    public static int evalFiveCards(int c1, int c2, int c3, int c4, int c5) {
        if (allSuitsAreEqual(c1, c2, c3, c4, c5)) {
            int index = (c1 | c2 | c3 | c4 | c5) >> 16;
            return FLUSH_TABLE[index];
        }

        int product = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
        return UNIQUE_FIVE_CARD_COMBINATION[find(product)];
    }

    private static boolean allSuitsAreEqual(int c1, int c2, int c3, int c4, int c5) {
        return (c1 & c2 & c3 & c4 & c5 & 0xF000) != 0;
    }

    public static int evalSevenCards(int[] sevenCards) {
        if (sevenCards.length != 7) {
            throw new IllegalArgumentException("Invalid number of cards");
        }

        int bestScore = MAX_VALUE;
        for (int[] combo : SEVEN_CARDS_COMBINATION) {
            int score = evalFiveCards(sevenCards[combo[0]], sevenCards[combo[1]], sevenCards[combo[2]], sevenCards[combo[3]], sevenCards[combo[4]]);

            if (score < bestScore) {
                bestScore = score;
            }
        }

        return bestScore;
    }
}
