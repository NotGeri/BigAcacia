package dev.geri.bigacacia.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public record CustomBlock(Material material, int offsetX, int offsetY, int offsetZ) {
    public Material getMaterial() {
        return material;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public Block getBlock(Location location) {
        if (location.getWorld() == null) return null;
        return location.getWorld().getBlockAt(location.getBlockX() + offsetX, location.getBlockY() + offsetY, location.getBlockZ() + offsetZ);
    }
}