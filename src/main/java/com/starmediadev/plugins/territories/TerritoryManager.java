package com.starmediadev.plugins.territories;

import com.starmediadev.data.manager.DatabaseManager;

public class TerritoryManager {
    private final Territories plugin;
    private final DatabaseManager databaseManager;

    public TerritoryManager(Territories territories) {
        this.plugin = territories;
        this.databaseManager = territories.getDatabaseManager();
    }
    
    
}
