package dev.geri.bigacacia.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;

public class TreeUtils {

    private final Material saplingMaterial;

    public TreeUtils(Material saplingMaterial) {
        this.saplingMaterial = saplingMaterial;
    }

    /**
     * Get a list of valid found blocks given the position of a sapling
     *
     * @param saplingPosition The position of the sapling to check
     * @return A list of valid blocks that match its material. Valid if there are 4 total, invalid if less
     */
    public ArrayList<Block> getValidSaplingBlocks(Location saplingPosition) {
        World world = saplingPosition.getWorld();
        ArrayList<Block> foundBlocks = new ArrayList<>();
        Block saplingBlock = saplingPosition.getBlock();
        foundBlocks.add(saplingBlock);

        if (world == null) return foundBlocks;
        if (saplingPosition.getBlock().getType() != saplingMaterial) return foundBlocks;

        Bukkit.broadcastMessage("Checking north"); // debug
        ArrayList<Block> northSide = getNorthSouthSide(saplingBlock, BlockFace.NORTH);
        if (northSide.size() == 3) {
            Bukkit.broadcastMessage("North good"); // debug
            foundBlocks.addAll(northSide);
        } else {
            Bukkit.broadcastMessage("North bad, checking south"); // debug
            ArrayList<Block> southSide = getNorthSouthSide(saplingBlock, BlockFace.SOUTH);
            if (southSide.size() == 3) {
                Bukkit.broadcastMessage("South good"); // debug
                foundBlocks.addAll(southSide);
            } else {
                Bukkit.broadcastMessage("South bad, we bad"); // debug
                // not valid
            }
        }

        return foundBlocks;
    }

    /**
     * Get the top or bottom part of a sapling pattern
     *
     * @param saplingBlock The sapling block to check
     * @param northSouth   Whether it's {@link BlockFace} <code>NORTH</code> or <code>SOUTH</code>
     * @return A list of valid blocks that match this part of the pattern. Valid if there are 3 total, invalid if less
     */
    private ArrayList<Block> getNorthSouthSide(Block saplingBlock, BlockFace northSouth) {
        if (northSouth != BlockFace.SOUTH && northSouth != BlockFace.NORTH) throw new IllegalArgumentException("getNorthSouthSide() can only accept BlockFace.NORTH or BlockFace.SOUTH!");

        ArrayList<Block> foundBlocks = new ArrayList<>();

        Block sideBlock = getSideBlock(saplingBlock, northSouth);
        if (sideBlock != null && sideBlock.getType() == saplingMaterial) {
            foundBlocks.add(sideBlock);

            Bukkit.broadcastMessage("  Checking west corner"); // debug
            ArrayList<Block> cornerBlocks = getCorner(saplingBlock, northSouth == BlockFace.NORTH ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST);
            if (cornerBlocks.size() == 2) {
                Bukkit.broadcastMessage("  West corner good"); // debug
                foundBlocks.addAll(cornerBlocks);
            } else {
                Bukkit.broadcastMessage("  West bad, checking east corner"); // debug
                cornerBlocks = getCorner(saplingBlock, northSouth == BlockFace.NORTH ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
                if (cornerBlocks.size() == 2) {
                    Bukkit.broadcastMessage("  East corner good"); // debug
                    foundBlocks.addAll(cornerBlocks);
                } else {
                    Bukkit.broadcastMessage("  East corner bad, we bad"); // debug
                }
            }
        }

        return foundBlocks;
    }

    /**
     * Get the corner to pieces of a sapling pattern
     *
     * @param saplingBlock The sapling block to check
     * @param corner       Which of the following corner it should be checking: {@link BlockFace}: <code>NORTH_WEST</code>, <code>NORTH_EAST</code>, <code>SOUTH_WEST</code> or <code>SOUTH_EAST</code>
     * @return A list of valid blocks that match this part of the pattern. Valid if there are 2 total, invalid if less
     */
    private ArrayList<Block> getCorner(Block saplingBlock, BlockFace corner) {

        ArrayList<Block> foundBlocks = new ArrayList<>();

        Block cornerBlock = saplingBlock.getRelative(corner);
        if (cornerBlock.getType() == saplingMaterial) {
            Bukkit.broadcastMessage("   Corner good"); // debug
            foundBlocks.add(cornerBlock);

            // Check west block
            Bukkit.broadcastMessage("   Checking side block"); // debug
            Block sideBlock = getSideBlock(saplingBlock, corner == BlockFace.NORTH_WEST || corner == BlockFace.SOUTH_WEST ? BlockFace.WEST : BlockFace.EAST);
            if (sideBlock != null) {
                Bukkit.broadcastMessage("   Side block good"); // debug
                foundBlocks.add(sideBlock);
            } else {
                Bukkit.broadcastMessage("   Side block bad"); // debug
            }
        } else {
            Bukkit.broadcastMessage("    Corner bad"); // debug
        }

        return foundBlocks;
    }

    /**
     * Get the side block of a specific sapling
     *
     * @param saplingBlock The sapling block to check
     * @param side         The {@link BlockFace} to check from that block
     * @return A valid {@link Block} or null if not found
     */
    private Block getSideBlock(Block saplingBlock, BlockFace side) {
        Block sideBlock = saplingBlock.getRelative(side);
        if (sideBlock.getType() == saplingMaterial) {
            return sideBlock;
        } else {
            return null;
        }
    }

}
