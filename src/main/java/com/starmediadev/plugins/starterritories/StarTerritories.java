package com.starmediadev.plugins.starterritories;

import com.starmediadev.plugins.starterritories.plot.*;
import org.bukkit.plugin.java.JavaPlugin;

public class StarTerritories extends JavaPlugin {
    
    private PlotManager plotManager;
    
    @Override
    public void onEnable() {
        plotManager = new PlotManager(this);
    }
    
    @Override
    public void onDisable() {
        plotManager.saveData();
    }
}
