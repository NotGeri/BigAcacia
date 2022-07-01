package dev.geri.bigacacia.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public record CustomBlock(BlockData blockData, int offsetX, int offsetY, int offsetZ) {

    /**
     * @return The offset X of the block from the origin sapling
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * @return The offset Y of the block from the origin sapling
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * @return The offset Z of the block from the origin sapling
     */
    public int getOffsetZ() {
        return offsetZ;
    }

    /**
     * Get to deserialized version of the block in a specific location
     *
     * @param location The location of the origin sapling
     * @return The deserialized block
     */
    public Block getBlock(Location location) {
        if (location.getWorld() == null) return null;
        return location.getWorld().getBlockAt(location.getBlockX() + offsetX, location.getBlockY() + offsetY, location.getBlockZ() + offsetZ);
    }

    /**
     * @return The raw data of the block including {@link org.bukkit.Material}, rotation, etc
     */
    public BlockData getBlockData() {
        return blockData;
    }

}