package com.starmediadev.plugins.territories;

import com.starmediadev.data.annotations.TableInfo;
import com.starmediadev.data.manager.DatabaseManager;

@TableInfo(tableName = "territories")
public class TerritoryManager {
    private final Territories plugin;
    private final DatabaseManager databaseManager;

    public TerritoryManager(Territories territories) {
        this.plugin = territories;
        this.databaseManager = territories.getDatabaseManager();
    }
    
    
}
