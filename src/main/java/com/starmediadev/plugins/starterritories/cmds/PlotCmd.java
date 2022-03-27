package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.plugins.starterritories.utils.TUtils;
import com.starmediadev.utils.helper.TimeHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class PlotCmd implements TabExecutor {
    
    private StarTerritories plugin;
    
    public PlotCmd(StarTerritories plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cYou must be a player to use that command."));
            return true;
        }
        
        if (!(args.length > 0)) {
            player.sendMessage(MCUtils.color("&cYou must provide a sub command."));
            return true;
        }
        
        Plot plot = plugin.getPlotManager().getPlot(player.getLocation());
        if (plot == null) {
            player.sendMessage(MCUtils.color("&cCould not find a plot."));
            return true;
        }
        
        if (args[0].equalsIgnoreCase("info")) {
            player.sendMessage(MCUtils.color("&eID: &b" + plot.getId()));
            player.sendMessage(MCUtils.color("&eType: &b" + plot.getPlotData().getPlotType().name()));
            player.sendMessage(MCUtils.color("&eOwner: " + plot.getOwner()));
            if (plot.getClaimInfo() != null) {
                player.sendMessage(MCUtils.color("&eDate Claimed: &e" + TimeHelper.formatDate(plot.getClaimInfo().getClaimDate())));
                player.sendMessage(MCUtils.color("&eClaimed By: &e" + Bukkit.getOfflinePlayer(plot.getClaimInfo().getClaimedBy()).getName()));
            } else {
                player.sendMessage(MCUtils.color("&eClaimed: &bNo"));
            }
        } else if (args[0].equalsIgnoreCase("flag")) {
            TUtils.handleFlagableFlagSubCommand(player, args, plot);
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
    
        if (args.length == 1) {
            List<String> subCommands = List.of("info", "flag");
            List<String> completions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("flag")) {
            List<String> subCommands = List.of("view", "set");
            List<String> completions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[1].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("flag")) {
            List<String> completions = new LinkedList<>();
            for (Flags flag : Flags.values()) {
                if (flag.name().toLowerCase().startsWith(args[2])) {
                    completions.add(flag.name().toLowerCase());
                }
            }
            Collections.sort(completions);
            return completions;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("flag")) {
            List<String> completions = new LinkedList<>();
            for (FlagValue flagValue : FlagValue.values()) {
                if (flagValue.name().toLowerCase().startsWith(args[3])) {
                    completions.add(flagValue.name().toLowerCase());
                }
            }
            
            Collections.sort(completions);
            return completions;
        }
    
        return null;
    }
}
