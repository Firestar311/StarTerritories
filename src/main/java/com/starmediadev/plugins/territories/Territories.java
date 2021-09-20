package com.starmediadev.plugins.territories;

import com.starmediadev.data.manager.DatabaseManager;
import com.starmediadev.plugins.framework.plot.PlotManager;
import com.starmediadev.plugins.territories.object.territory.TerritoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Territories extends JavaPlugin {
    private DatabaseManager databaseManager;
    private PlotManager plotManager;
    private TerritoryManager territoryManager;
    
    public void onEnable() {
        this.plotManager = getServer().getServicesManager().getRegistration(PlotManager.class).getProvider();
        this.territoryManager = new TerritoryManager(this);
        Bukkit.getServicesManager().register(TerritoryManager.class, territoryManager, this, ServicePriority.Highest);
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlotManager getPlotManager() {
        return plotManager;
    }

    public TerritoryManager getTerritoryManager() {
        return territoryManager;
    }
}
