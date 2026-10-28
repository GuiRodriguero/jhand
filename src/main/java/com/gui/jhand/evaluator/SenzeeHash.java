package com.gui.jhand.evaluator;

class SenzeeHash {

    private static final short[] ASSOC_TABLE = new short[65536];

    public static int find(int product) {
        int index = (product ^ (product >> 9)) & 0x1FFF;
        return ASSOC_TABLE[index];
    }

}
