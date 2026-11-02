package com.gui.jhand.evaluator;

import lombok.NoArgsConstructor;

import static com.gui.jhand.evaluator.HandEvaluatorTables.HASH_ADJUST;
import static com.gui.jhand.evaluator.HandEvaluatorTables.HASH_VALUES;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class SenzeeHash {

	static int find(int product) {
		int u = product + 0xe91aaa35;
		u ^= u >>> 16;
		u += u << 8;
		u ^= u >>> 4;
		int b = (u >>> 8) & 0x1ff;
		int a = (u + (u << 2)) >>> 19;
		int r = a ^ HASH_ADJUST[b];
		return HASH_VALUES[r];
	}

}
