package dev.geri.bigacacia.utils;

import dev.geri.bigacacia.BigAcacia;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class TreeUtils {

    private final Utils utils;
    private final Logger logger;
    private final BigAcacia plugin;
    private final FileConfiguration config;
    private final Material saplingMaterial;
    private final HashMap<String, Tree> trees = new HashMap<>();

    public TreeUtils(BigAcacia plugin, Utils utils, FileConfiguration config, Material saplingMaterial) {
        this.plugin = plugin;
        this.utils = utils;
        this.config = config;
        this.saplingMaterial = saplingMaterial;
        this.logger = plugin.getLogger();

        ConfigurationSection section = config.getConfigurationSection("trees");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection treeSection = section.getConfigurationSection(key);
                if (treeSection != null) {
                    Tree tree = this.loadTree(key, treeSection.getStringList("blocks"));
                    this.trees.put(key, tree);
                } else {
                    logger.warning("Tree " + key + " is not a valid configuration!");
                }
            }
            logger.info("Loaded " + trees.size() + " trees!");
        } else {
            logger.warning("There are no trees to load!");
        }
    }


    /**
     * Load a list of custom blocks from the config
     */
    public Tree loadTree(String name, List<String> rawBlocks) {
        ArrayList<CustomBlock> blocks = new ArrayList<>();

        for (String line : rawBlocks) {
            String[] lineSplit = line.split(" ");
            blocks.add(new CustomBlock(Bukkit.createBlockData(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]), Integer.parseInt(lineSplit[3])));
        }

        return new Tree(name, blocks);
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

        utils.sendDebugMessage("Checking north side");
        ArrayList<Block> northSide = getNorthSouthSide(saplingBlock, BlockFace.NORTH);
        if (northSide.size() == 3) {
            utils.sendDebugMessage("North side valid");
            foundBlocks.addAll(northSide);
        } else {
            utils.sendDebugMessage("North side invalid ➜ checking south side");
            ArrayList<Block> southSide = getNorthSouthSide(saplingBlock, BlockFace.SOUTH);
            if (southSide.size() == 3) {
                utils.sendDebugMessage("South side valid");
                foundBlocks.addAll(southSide);
            } else {
                utils.sendDebugMessage("South side invalid ➜ structure invalid");
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

            utils.sendDebugMessage("  Checking west corner");
            ArrayList<Block> cornerBlocks = getCorner(saplingBlock, northSouth == BlockFace.NORTH ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST);
            if (cornerBlocks.size() == 2) {
                utils.sendDebugMessage("  West corner valid");
                foundBlocks.addAll(cornerBlocks);
            } else {
                utils.sendDebugMessage("  West corner invalid ➜ checking east corner");
                cornerBlocks = getCorner(saplingBlock, northSouth == BlockFace.NORTH ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
                if (cornerBlocks.size() == 2) {
                    utils.sendDebugMessage("  East corner valid");
                    foundBlocks.addAll(cornerBlocks);
                } else {
                    utils.sendDebugMessage("  East corner invalid ➜ structure invalid");
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
            utils.sendDebugMessage("  " + corner.name().toLowerCase() + " corner valid");
            foundBlocks.add(cornerBlock);

            // Check side block
            utils.sendDebugMessage("   Checking side block");
            Block sideBlock = getSideBlock(saplingBlock, corner == BlockFace.NORTH_WEST || corner == BlockFace.SOUTH_WEST ? BlockFace.WEST : BlockFace.EAST);
            if (sideBlock != null) {
                utils.sendDebugMessage("   Side block valid");
                foundBlocks.add(sideBlock);
            } else {
                utils.sendDebugMessage("   Side block invalid");
            }
        } else {
            utils.sendDebugMessage("   " + corner.name().toLowerCase() + " corner invalid");
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

    public void saveTree(String name, Location posOrigin, Location pos1, Location pos2) {

        World world = pos1.getWorld();
        if (world != pos2.getWorld()) throw new InvalidParameterException("The two worlds did not match!");

        Location lowerPosition, higherPosition;
        if (pos1.getBlockY() > pos2.getBlockY()) {
            higherPosition = pos1;
            lowerPosition = pos2;
        } else {
            higherPosition = pos2;
            lowerPosition = pos1;
        }

        ArrayList<String> deserializedBlocks = new ArrayList<>();
        ArrayList<CustomBlock> blocks = new ArrayList<>();

        for (int y = lowerPosition.getBlockY(); y <= higherPosition.getBlockY(); y++) {

            int[] xValues = getLargerValue(lowerPosition.getBlockX(), higherPosition.getBlockX());

            while (xValues[1] <= xValues[0]) {

                int[] zValues = getLargerValue(lowerPosition.getBlockZ(), higherPosition.getBlockZ());

                while (zValues[1] <= zValues[0]) {
                    Block block = world.getBlockAt(xValues[1], y, zValues[1]);
                    if (block.getType() != Material.AIR) {
                        Location relative = block.getLocation().subtract(posOrigin);

                        blocks.add(new CustomBlock(block.getBlockData(), relative.getBlockX(), relative.getBlockY(), relative.getBlockZ()));
                        deserializedBlocks.add(String.format("%s %s %s %s", block.getBlockData().getAsString(true), relative.getBlockX(), relative.getBlockY(), relative.getBlockZ()));
                    }

                    zValues[1]++;
                }

                xValues[1]++;
            }

        }

        Tree tree = new Tree(name, blocks);
        this.trees.put(tree.getName(), tree);

        ConfigurationSection configSection = config.createSection("trees." + name);
        configSection.set("blocks", deserializedBlocks);
        plugin.saveConfig();
    }

    private int[] getLargerValue(int value1, int value2) {
        int[] values = new int[2];

        if (value1 > value2) {
            values[0] = value1;
            values[1] = value2;
        } else {
            values[0] = value2;
            values[1] = value1;
        }

        return values;
    }

    /**
     * Get all the {@link CustomBlock}-s of a random tree
     *
     * @return A list of all the blocks of a selected random tree or null if there are none loaded
     */
    public ArrayList<CustomBlock> getRandomTreeBlocks() {
        if (trees.isEmpty()) return null;

        Random random = new Random();
        int index = random.nextInt(0, trees.size());

        Tree[] values = trees.values().toArray(new Tree[0]);

        return values[index].getBlocks();
    }

    /**
     * @return All loaded {@link Tree}-s
     */
    public HashMap<String, Tree> getTrees() {
        return this.trees;
    }

    /**
     * Delete a loaded tree
     *
     * @param name The name of the tree
     */
    public void deleteTree(String name) {
        this.trees.remove(name);
        config.set("trees." + name, null);
        plugin.saveConfig();
    }

    /**
     * Get a loaded tree by its name
     *
     * @param name The name of tree
     * @return The found {@link Tree} or null if not found
     */
    public Tree getTreeByName(String name) {
        return trees.get(name);
    }

}
