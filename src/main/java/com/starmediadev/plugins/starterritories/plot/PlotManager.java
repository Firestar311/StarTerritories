package com.starmediadev.plugins.starterritories.plot;

import com.starmediadev.plugins.starmcutils.util.Config;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.utils.Utils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class PlotManager {
    
    private Map<String, Plot> plots = new HashMap<>();
    private Config config;
    
    public PlotManager(StarTerritories plugin) {
        config = new Config(plugin, "plots.yml");
        config.setup();
        
        loadData();
        
        Bukkit.getScheduler().runTaskTimer(plugin, this::saveData, 1L, 6000);
    }
    
    public void loadData() {
        ConfigurationSection plotsSection = config.getConfigurationSection("plots");
        if (plotsSection == null) {
            return;
        }
        
        for (String plotId : plotsSection.getKeys(false)) {
            String worldName = plotsSection.getString(plotId + ".world");
            int x = Integer.parseInt(plotsSection.getString(plotId + ".x"));
            int z = Integer.parseInt(plotsSection.getString(plotId + ".z"));
            Plot plot = new Plot(plotId, worldName, x, z);
            this.plots.put(plotId, plot);
        }
    }
    
    public void saveData() {
        config.set("plots", null);
        config.save();
        plots.forEach((id, plot) -> {
            String basePath = "plots." + id;
            config.set(basePath + ".world", plot.getWorldName());
            config.set(basePath + ".x", plot.getX() + "");
            config.set(basePath + ".z", plot.getZ() + "");
        });
        config.save();
    }
    
    public Plot getPlot(Location location) {
        for (Plot plot : this.plots.values()) {
            if (plot.contains(location)) {
                return plot;
            }
        }
        
        Plot plot = createPlot(location.getChunk());
        this.plots.put(plot.getPlotId(), plot);
        return plot;
    }
    
    public Plot createPlot(Chunk chunk) {
        String plotId;
        do {
            plotId = Utils.generateCode(8, false, true, false);
        } while (plots.containsKey(plotId));
        
        Plot plot = new Plot(plotId, chunk.getWorld().getName(), chunk);
        this.plots.put(plotId, plot);
        return plot;
    }
}
