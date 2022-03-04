package com.starmediadev.plugins.territories;

import com.starmediadev.plugins.plotframework.PlotManager;
import com.starmediadev.plugins.territories.object.territory.TerritoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Territories extends JavaPlugin {
    private PlotManager plotManager;
    private TerritoryManager territoryManager;
    
    public void onEnable() {
        this.plotManager = getServer().getServicesManager().getRegistration(PlotManager.class).getProvider();
        this.territoryManager = new TerritoryManager(this);
        Bukkit.getServicesManager().register(TerritoryManager.class, territoryManager, this, ServicePriority.Highest);
    }

    public PlotManager getPlotManager() {
        return plotManager;
    }

    public TerritoryManager getTerritoryManager() {
        return territoryManager;
    }
}
