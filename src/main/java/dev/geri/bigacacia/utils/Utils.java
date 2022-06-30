package dev.geri.bigacacia.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class Utils {

    /**
     * Load a list of custom blocks from the config
     */
    public ArrayList<CustomBlock> loadBlocks() {
        ArrayList<CustomBlock> blocks = new ArrayList<>();

        String bap = """
                STONE 0 0 0
                STONE 1 0 0
                STONE 1 0 1
                STONE 0 0 1
                """;

        for (String line : bap.split("\n")) {
            String[] lineSplit = line.split(" ");
            blocks.add(new CustomBlock(Material.getMaterial(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]), Integer.parseInt(lineSplit[3])));
        }

        return blocks;
    }


    /**
     * Format a message with colour codes
     *
     * @param message The message to format
     * @return The formatted message
     */
    public String formatMessage(String message, Object... objects) {
        // Todo: Add HEX support
        return ChatColor.translateAlternateColorCodes('&', String.format(message, objects));
    }

}
