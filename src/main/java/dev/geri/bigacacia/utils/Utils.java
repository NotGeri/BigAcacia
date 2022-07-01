package dev.geri.bigacacia.utils;

import org.bukkit.ChatColor;

public class Utils {

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
