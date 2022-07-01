package dev.geri.bigacacia.utils;

import java.util.ArrayList;

public class Tree {

    private final String name;
    private final int chance;
    private final ArrayList<CustomBlock> blocks;

    public Tree(String name, ArrayList<CustomBlock> blocks, int chance) {
        this.name = name;
        this.blocks = blocks;
        this.chance = chance;
    }

    /**
     * @return The name of the tree
     */
    public String getName() {
        return name;
    }

    /**
     * @return The % chance to spawn this tree
     */
    public int getChance() {
        return chance;
    }

    /**
     * @return All the blocks to spawn for this tree
     */
    public ArrayList<CustomBlock> getBlocks() {
        return blocks;
    }

}
