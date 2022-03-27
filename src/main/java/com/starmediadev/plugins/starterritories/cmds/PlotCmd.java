package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
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
            if (!(args.length > 1)) {
                player.sendMessage(MCUtils.color("&cYou must provide a flag sub command."));
                return true;
            }
            
            if (!(args.length > 2)) {
                player.sendMessage(MCUtils.color("&cYou must provide a flag name"));
                return true;
            }
    
            Flags flag;
            try {
                flag = Flags.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage("&cInvalid flag value: " + args[2]);
                return true;
            }
    
            if (args[1].equalsIgnoreCase("view")) {
                FlagValue flagValue = plot.getFlags().get(flag.name()).getValue();
                player.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + flagValue.name().toLowerCase() + " &ein plot &b" + plot.getId()));
            } else if (args[1].equalsIgnoreCase("set")) {
                FlagValue flagValue;
                try {
                    flagValue = FlagValue.valueOf(args[3].toUpperCase());
                } catch (Exception e) {
                    player.sendMessage(MCUtils.color("&cInvalid flag value: " + args[3]));
                    return true;
                }
    
                if (flagValue == FlagValue.DISABLED || flagValue == FlagValue.RESTRICTED) {
                    player.sendMessage("&cOnly allow, undefined and deny are allowed to be set by this command at this time");
                    return true;
                }
    
                plot.setFlag(flag, flagValue);
                player.sendMessage(MCUtils.color("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein plot &b" + plot.getId()));
            }
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
