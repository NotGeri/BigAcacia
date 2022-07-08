package dev.geri.bigacacia.utils;

import dev.geri.bigacacia.BigAcacia;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    private final BigAcacia plugin;

    public Utils(BigAcacia plugin) {
        this.plugin = plugin;
    }

    /**
     * Format a message with colour codes
     *
     * @param message The message to format
     * @return The formatted message
     */
    public String formatMessage(String message, Object... objects) {
        return ChatColor.translateAlternateColorCodes('&', String.format(message, objects));
    }

    /**
     * Send a debug message to staff members if debug mode is enabled
     *
     * @param message The message to format & send
     */
    public void sendDebugMessage(String message) {
        if (!plugin.debugMode()) return;
        plugin.getLogger().warning(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("bigacacia.debug")) return;
            player.sendMessage(formatMessage("&8[&3Debug&8]&r " + message));
        }
    }

}
