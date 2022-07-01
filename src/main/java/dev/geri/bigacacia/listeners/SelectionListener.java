package dev.geri.bigacacia.listeners;

import dev.geri.bigacacia.BigAcacia;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SelectionListener implements Listener {

    private final BigAcacia plugin;
    private final Utils utils;

    public SelectionListener(BigAcacia plugin, Utils utils) {
        this.plugin = plugin;
        this.utils = utils;
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

        // Check for sneaking
        if (player.isSneaking()) {

            // Check permission
            if (!player.hasPermission("bigacacia.selectorigin")) {
                player.sendMessage(utils.formatMessage("&4ERROR&f: &cYou do not have access to this action!"));
                return;
            }

            plugin.setPosition(player, 0, location);
            player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aPosition &2origin &aset to &2%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));

            return;
        }

        // Check action
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

            // Check permission
            if (!player.hasPermission("bigacacia.select1")) {
                player.sendMessage(utils.formatMessage("&4ERROR&f: &cYou do not have access to this action!"));
                return;
            }

            plugin.setPosition(player, 1, location);
            player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aPosition &2#1 &aset to &2%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));

        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // Check permission
            if (!player.hasPermission("bigacacia.select2")) {
                player.sendMessage(utils.formatMessage("&4ERROR&f: &cYou do not have access to this action!"));
                return;
            }

            plugin.setPosition(player, 2, location);
            player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aPosition &2#2 &aset to &2%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }

    }

}
