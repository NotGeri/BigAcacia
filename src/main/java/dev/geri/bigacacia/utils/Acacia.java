package dev.geri.bigacacia.utils;

import java.util.ArrayList;

public class Acacia {

    private int chance;
    private ArrayList<CustomBlock> blocks;

    public Acacia(ArrayList<CustomBlock> blocks) {
        this.blocks = blocks;
    }

    public int getChance() {
        return chance;
    }

    public ArrayList<CustomBlock> getBlocks() {
        return blocks;
    }

    public Acacia setChance(int chance) {
        this.chance = chance;
        return this;
    }

    public Acacia setBlocks(ArrayList<CustomBlock> blocks) {
        this.blocks = blocks;
        return this;
    }

}
