package dev.geri.bigacacia.commands;

import dev.geri.bigacacia.BigAcacia;
import dev.geri.bigacacia.utils.Tree;
import dev.geri.bigacacia.utils.TreeUtils;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcaciaCommand implements TabExecutor {

    private final BigAcacia plugin;
    private final Utils utils;
    private final TreeUtils treeUtils;

    public AcaciaCommand(BigAcacia plugin, Utils utils, TreeUtils treeUtils) {
        this.plugin = plugin;
        this.utils = utils;
        this.treeUtils = treeUtils;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou must be a player to use this command!"));
            return true;
        }

        if (args.length == 0) {
            if (!player.hasPermission("bigacacia.help")) {
                player.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou do not have access to this command!"));
                return true;
            }

            this.sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "save" -> {
                if (!player.hasPermission("bigacacia.save")) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou do not have access to this command!"));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cPlease provide a variation name!"));
                    return true;
                }

                String variation = args[1];

                Location posOrigin = plugin.getPosition(player, 0);
                Location pos1 = plugin.getPosition(player, 1);
                Location pos2 = plugin.getPosition(player, 2);

                if (posOrigin == null) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cPosition &4origin &cis not set!"));
                    return true;
                }

                if (pos1 == null) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cPosition &4#1 &cis not set!"));
                    return true;
                }

                if (pos2 == null) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cPosition &4#2 &cis not set!"));
                    return true;
                }

                try {
                    treeUtils.saveTree(variation, posOrigin, pos1, pos2);
                } catch (Exception exception) {
                    exception.printStackTrace();//todo
                    return true;
                }

                player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aSaved!"));
            }

            case "list" -> {
                if (!player.hasPermission("bigacacia.list")) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou do not have access to this command!"));
                    return true;
                }

                StringBuilder sb = new StringBuilder("\n&2&lAVAILABLE TREES&f:");
                for (Map.Entry<String, Tree> treeEntry : treeUtils.getTrees().entrySet()) {
                    sb.append("\n").append("&f• &a")
                            .append(treeEntry.getKey())
                            .append(" &8(")
                            .append("&7")
                            .append(treeEntry.getValue().getChance())
                            .append("%% chance")
                            .append("&8)");
                }

                player.sendMessage(utils.formatMessage(sb.append("\n&f").toString()));

            }

            case "delete" -> {
                if (!player.hasPermission("bigacacia.delete")) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou do not have access to this command!"));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cPlease provide a variation name!"));
                    return true;
                }

                String name = args[1];
                Tree tree = treeUtils.getTreeByName(name);

                if (tree == null) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cNo tree was found with the name &4%s&c!", name));
                    return true;
                }

                treeUtils.deleteTree(name);
                player.sendMessage(utils.formatMessage("&2&lSUCCESS&f: &aDeleted &2%s&a!", name));
            }

            default -> {
                if (!player.hasPermission("bigacacia.help")) {
                    player.sendMessage(utils.formatMessage("&4&lERROR&f: &cYou do not have access to this command!"));
                    return true;
                }

                this.sendHelp(player);
            }
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(utils.formatMessage("""
                &b
                &3&lAVAILABLE COMMANDS&f:
                &f• &b/acacia save
                &f• &b/acacia list
                &f• &b/acacia delete
                &b
                """));
    }

    private final HashMap<String, String> subCommands = new HashMap<>() {{
        this.put("bigacacia.save", "save");
        this.put("bigacacia.list", "list");
        this.put("bigacacia.delete", "delete");
    }};

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> arguments = new ArrayList<>();

        for (Map.Entry<String, String> subCommand : subCommands.entrySet()) {
            if (sender.hasPermission(subCommand.getKey())) arguments.add(subCommand.getValue());
        }

        if (args.length == 1) {
            for (String result : arguments) {
                if (result.startsWith(args[0])) results.add(result);
            }
        }

        return results;
    }

}
