package dev.geri.bigacacia.commands;

import dev.geri.bigacacia.BigAcacia;
import dev.geri.bigacacia.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcaciaCommand implements TabExecutor {

    private final BigAcacia plugin;
    private final Utils utils;

    public AcaciaCommand(BigAcacia plugin, Utils utils) {
        this.plugin = plugin;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(utils.formatMessage("&4ERROR&f: &cYou must be a player to use this command!"));
            return true;
        }

        return true;
    }

    private final HashMap<String, String> subCommands = new HashMap<>() {{
        this.put("bigacacia.save", "save");
        this.put("bigacacia.list", "list");
        this.put("bigacacia.delete", "delete");
    }};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        for (Map.Entry<String, String> subCommand : subCommands.entrySet()) {
            if (sender.hasPermission(subCommand.getKey())) results.add(subCommand.getValue());
        }

        return results;
    }
}
