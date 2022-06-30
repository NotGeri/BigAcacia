package dev.geri.bigacacia;

import dev.geri.bigacacia.commands.AcaciaCommand;
import dev.geri.bigacacia.utils.CustomBlock;
import dev.geri.bigacacia.utils.TreeUtils;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public final class BigAcacia extends JavaPlugin implements Listener {

    private final Material saplingMaterial = Material.ACACIA_SAPLING;

    private ArrayList<CustomBlock> blocks;
    private final Utils utils = new Utils();
    private final TreeUtils treeUtils = new TreeUtils(saplingMaterial);
    private final Logger logger = this.getLogger();
    private final HashMap<Player, Location> position1s = new HashMap<>();
    private final HashMap<Player, Location> position2s = new HashMap<>();

    @Override
    public void onEnable() {

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Register commands
        PluginCommand acaciaCommand = this.getCommand("acacia");
        if (acaciaCommand != null) {
            acaciaCommand.setExecutor(new AcaciaCommand(this, utils));
        } else {
            logger.warning("Unable to register acacia command!");
        }

        // Load blocks
        this.blocks = utils.loadBlocks();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check used item
        if (item.getType() != Material.WOODEN_HOE) return;

        // Check hand
        if (e.getHand() != EquipmentSlot.HAND) return;

        // Cancel event
        e.setCancelled(true);

        // Check if block exists
        if (block == null) return;
        Location location = block.getLocation();

        // Check action
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

            // Check permission
            if (!player.hasPermission("bigacacia.select1")) {
                player.sendMessage(utils.formatMessage("&4ERROR&f: &cYou do not have access to this action!"));
                return;
            }

            this.position1s.put(player, location);
            player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aPosition &2#1 &aset to &2%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // Check permission
            if (!player.hasPermission("bigacacia.select2")) {
                player.sendMessage(utils.formatMessage("&4ERROR&f: &cYou do not have access to this action!"));
                return;
            }

            this.position2s.put(player, location);
            player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aPosition &2#2 &aset to &2%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }

    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        if (e.getSpecies() != TreeType.ACACIA) return;
        Location location = e.getLocation();

        ArrayList<Block> validSaplingBlocks = treeUtils.getValidSaplingBlocks(location);

        // Check if the spot was valid
        if (validSaplingBlocks.size() != 4) return;

        e.setCancelled(true);

        for (Block block : validSaplingBlocks) {
            block.setType(Material.DIAMOND_BLOCK);
        }

        for (CustomBlock block : blocks) {
            Block b = block.getBlock(location);
            if (b != null) b.setType(block.getMaterial());
        }

        e.getBlocks().clear();
    }

    /**
     * @param player   The player the check it for
     * @param position 1 for first position, 2 for second position
     * @return The selected position of that player or null if not stored
     */
    public Location getPosition(Player player, int position) {
        if (position == 1) return position1s.get(player);
        else return position2s.get(player);
    }

}