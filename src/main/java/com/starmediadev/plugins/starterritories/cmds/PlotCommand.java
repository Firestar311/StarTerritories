package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.command.*;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.utils.helper.TimeHelper;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class PlotCommand extends StarCommand {
    public PlotCommand(StarTerritories plugin) {
        super("plot", "Plot based management", "starterritories.plot.command", true, false, new ArrayList<>());
        
        addSubCommand(new SubCommand(this, "info", "View information about the plot you are standing in.", "") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                Plot plot = plugin.getPlotManager().getPlot(actor.getPlayer().getLocation());
                actor.sendMessage("&eID: &b" + plot.getId());
                actor.sendMessage("&eType: &b" + plot.getPlotData().getPlotType().name());
                actor.sendMessage("&eOwner: " + plot.getOwner());
                if (plot.getClaimInfo() != null) {
                    actor.sendMessage("&eDate Claimed: &e" + TimeHelper.formatDate(plot.getClaimInfo().getClaimDate()));
                    actor.sendMessage("&eClaimed By: &e" + Bukkit.getOfflinePlayer(plot.getClaimInfo().getClaimedBy()).getName());
                } else {
                    actor.sendMessage("&eClaimed: &bNo");
                }
            }
        });
        addSubCommand(new FlagSubCommand<>(this, plugin, Plot.class));
    }
}
