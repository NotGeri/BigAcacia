package dev.geri.bigacacia.listeners;

import dev.geri.bigacacia.BigAcacia;
import dev.geri.bigacacia.utils.CustomBlock;
import dev.geri.bigacacia.utils.TreeUtils;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GrowthListener implements Listener {

    private final Utils utils;
    private final Logger logger;
    private final TreeUtils treeUtils;

    public GrowthListener(BigAcacia plugin, Utils utils, TreeUtils treeUtils) {
        this.utils = utils;
        this.logger = plugin.getLogger();
        this.treeUtils = treeUtils;
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
            if (northWestCorner == null) {
                northWestCorner = block.getLocation();
            }

            if (block.getX() < northWestCorner.getBlockX() || block.getZ() < northWestCorner.getBlockZ()) {
                northWestCorner = block.getLocation();
            }
        }

        // Check if corner is valid
        if (northWestCorner == null) {
            utils.sendDebugMessage("A tree was not able to generate even though the blocks were valid!");
            e.setCancelled(true);
            return;
        }

        ArrayList<CustomBlock> blocks = treeUtils.getRandomTreeBlocks();
        if (blocks == null) {
            logger.warning("A 2x2 acacia tree was attempted to be grown but there are no trees loaded!");
            return;
        }

        // See any existing blocks
        boolean cancel = false;
        for (CustomBlock customBlock : blocks) {
            Block block = customBlock.getBlock(northWestCorner);

            // Check if it's not air, the sapling or leaves
            if (block == null || (block.getType() != Material.AIR && block.getType() != Material.ACACIA_LOG && block.getType() != Material.ACACIA_SAPLING && !Tag.LEAVES.isTagged(block.getType()))) {
                cancel = true;

                utils.sendDebugMessage("There was a block blocking tree growth: " + (block != null ? block.toString() : customBlock.toString()));

                if (block != null) {
                    block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getX() + 0.5, block.getY()+0.5, block.getZ() + 0.5, 50, 0.5, 0.5, 0.5 , 1, block.getType().createBlockData());
                }
            }
        }

        if (cancel) {
            e.setCancelled(true);
            return;
        }

        // Delete saplings just in case
        for (Block sapling : validSaplingBlocks) {
            if (sapling.getType() == Material.ACACIA_SAPLING) sapling.setType(Material.AIR);
        }

        // Set blocks
        for (CustomBlock customBlock : blocks) {
            Block block = customBlock.getBlock(northWestCorner);
            if (block != null) {
                block.setBlockData(customBlock.getBlockData());
            }
        }

        // Clear original tree
        e.getBlocks().clear();
    }


}
