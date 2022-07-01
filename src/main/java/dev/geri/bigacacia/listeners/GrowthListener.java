package dev.geri.bigacacia.listeners;

import dev.geri.bigacacia.utils.CustomBlock;
import dev.geri.bigacacia.utils.TreeUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GrowthListener implements Listener {

    private final Logger logger;
    private final TreeUtils treeUtils;

    public GrowthListener(Logger logger, TreeUtils treeUtils) {
        this.treeUtils = treeUtils;
        this.logger = logger;
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        if (e.getSpecies() != TreeType.ACACIA) return;
        Location location = e.getLocation();

        ArrayList<Block> validSaplingBlocks = treeUtils.getValidSaplingBlocks(location);

        // Check if the spot was valid
        if (validSaplingBlocks.size() != 4) return;

        // Cancel event
        e.setCancelled(true);

        // Get north-west corner
        Location northWestCorner = null;
        for (Block block : validSaplingBlocks) {
            block.setType(Material.AIR);

            if (northWestCorner == null) northWestCorner = block.getLocation();

            if (block.getX() < northWestCorner.getBlockX() || block.getZ() < northWestCorner.getBlockZ()) {
                northWestCorner = block.getLocation();
            }
        }

        // Check if corner is valid
        if (northWestCorner == null) {
            logger.warning("A tree was not able to generate even though the blocks were valid!");
            e.setCancelled(true);
            return;
        }

        // Set blocks
        for (CustomBlock block : treeUtils.getRandomTreeBlocks()) {
            Block b = block.getBlock(northWestCorner);
            if (b != null) b.setBlockData(block.getBlockData());
        }

        // Debug: remove
        northWestCorner.getWorld().getBlockAt(northWestCorner).setType(Material.REDSTONE_BLOCK);

        // Clear original tree
        e.getBlocks().clear();
    }


}
