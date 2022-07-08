package dev.geri.bigacacia.utils;

import java.util.ArrayList;

public class Tree {

    private final String name;
    private final ArrayList<CustomBlock> blocks;

    public Tree(String name, ArrayList<CustomBlock> blocks) {
        this.name = name;
        this.blocks = blocks;
    }

    /**
     * @return The name of the tree
     */
    public String getName() {
        return name;
    }

    /**
     * @return All the blocks to spawn for this tree
     */
    public ArrayList<CustomBlock> getBlocks() {
        return blocks;
    }

}
