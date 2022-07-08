package dev.geri.bigacacia;

import dev.geri.bigacacia.commands.AcaciaCommand;
import dev.geri.bigacacia.listeners.GrowthListener;
import dev.geri.bigacacia.listeners.SelectionListener;
import dev.geri.bigacacia.utils.TreeUtils;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public final class BigAcacia extends JavaPlugin implements Listener {

    private boolean debugMode = false;
    private final Utils utils = new Utils(this);
    private final Logger logger = this.getLogger();
    private final HashMap<Player, Location> pos1 = new HashMap<>();
    private final HashMap<Player, Location> pos2 = new HashMap<>();
    private final HashMap<Player, Location> posOrigin = new HashMap<>();
    private final Material saplingMaterial = Material.ACACIA_SAPLING;

    @Override
    public void onEnable() {

        // Load config
        FileConfiguration config = this.getConfig();
        this.saveDefaultConfig();
        this.treeUtils = new TreeUtils(this, config, saplingMaterial);

        // Register events
        Bukkit.getPluginManager().registerEvents(new GrowthListener(this, utils, treeUtils), this);
        Bukkit.getPluginManager().registerEvents(new SelectionListener(this, utils), this);

        // Register commands
        PluginCommand acaciaCommand = this.getCommand("acacia");
        if (acaciaCommand != null) {
            acaciaCommand.setExecutor(new AcaciaCommand(this, utils, treeUtils));
        } else {
            logger.warning("Unable to register acacia command!");
        }
    }

    /**
     * @param player   The player the check it for
     * @param position 0 for origin, 1 for first position, 2 for second position
     * @return The selected position of that player or null if not stored
     */
    public Location getPosition(Player player, int position) {
        Location loc = null;
        switch (position) {
            case 0 -> loc = posOrigin.get(player);
            case 1 -> loc = pos1.get(player);
            case 2 -> loc = pos2.get(player);
        }

        return loc;
    }

    /**
     * Set one of the selection positions of a player
     *
     * @param player   The player to set it for
     * @param position 0 for origin, 1 for first position, 2 for second position
     * @param location The location to set it to
     */
    public void setPosition(Player player, int position, Location location) {

        switch (position) {
            case 0 -> posOrigin.put(player, location);
            case 1 -> pos1.put(player, location);
            case 2 -> pos2.put(player, location);
        }
    }

    /**
     * @return Whether debug mode is eneabled
     */
    public boolean debugMode() {
        return debugMode;
    }

    /**
     * @param debugMode Whether debug mode should be enabled
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

}