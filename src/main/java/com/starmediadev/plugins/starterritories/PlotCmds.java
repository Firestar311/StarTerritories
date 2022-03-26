package com.starmediadev.plugins.starterritories;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.utils.helper.TimeHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.exception.CommandErrorException;

@Command("plot")
public class PlotCmds {
    @Dependency
    private StarTerritories plugin;
    
    
    @Subcommand("view")
    public void view(Player player) {
        Plot plot = plugin.getPlotManager().getPlot(player.getLocation());
        player.sendMessage(MCUtils.color("&eID: &b" + plot.getId()));
        player.sendMessage(MCUtils.color("&eType: &b" + plot.getPlotData().getPlotType().name()));
        player.sendMessage(MCUtils.color("&eOwner: " + plot.getOwner()));
        if (plot.getClaimInfo() != null) {
            player.sendMessage(MCUtils.color("&eDate Claimed: &e" + TimeHelper.formatDate(plot.getClaimInfo().getClaimDate())));
            player.sendMessage(MCUtils.color("&eClaimed By: &e" + Bukkit.getOfflinePlayer(plot.getClaimInfo().getClaimedBy()).getName()));
        } else {
            player.sendMessage(MCUtils.color("&eClaimed: &bNo"));
        }
    }
    
    @Subcommand("flag view")
    public void flagView(Player player, @Named("Flag Name") String flagName) {
        Plot plot = plugin.getPlotManager().getPlot(player.getLocation());
        Flags flag;
        try {
            flag = Flags.valueOf(flagName.toUpperCase());
        } catch (Exception e) {
            throw new CommandErrorException("Invalid flag name");
        }
        
        player.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + plot.getFlags().get(flag.name()).getValue().name().toLowerCase() + " &ein plot &b" + plot.getId()));
    }
    
    @Subcommand("flag set")
    public void flagSet(Player player, @Named("Flag Name") String flagName, @Named("Flag Value") String value) {
        Plot plot = plugin.getPlotManager().getPlot(player.getLocation());
        Flags flag;
        try {
            flag = Flags.valueOf(flagName.toUpperCase());
        } catch (Exception e) {
            throw new CommandErrorException("Invalid flag name");
        }
    
        FlagValue flagValue;
        try {
            flagValue = FlagValue.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new CommandErrorException("Invalid Flag value");
        }
        
        if (flagValue == FlagValue.DISABLED || flagValue == FlagValue.RESTRICTED) {
            throw new CommandErrorException("Only allow, undefined and deny are allowed to be set by this command");
        }
        
        plot.setFlag(flag, flagValue);
        player.sendMessage(MCUtils.color("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein plot &b" + plot.getId()));
    }
}