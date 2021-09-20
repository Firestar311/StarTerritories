package com.starmediadev.plugins.territories.object.territory;

import com.starmediadev.data.manager.DatabaseManager;
import com.starmediadev.plugins.territories.Territories;

public class TerritoryManager {
    private final Territories plugin;
    private final DatabaseManager databaseManager;

    public TerritoryManager(Territories territories) {
        this.plugin = territories;
        this.databaseManager = territories.getDatabaseManager();
    }
    
    
}
