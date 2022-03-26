package com.starmediadev.plugins.starterritories.objects.territory;

import java.util.*;

public class TerritoryManager {
    private Map<String, Territory> territories = new HashMap<>();
    
    public void addTerritory(Territory territory) {
        territories.put(territory.getId(), territory);
    }
    
    public Territory getTerritory(String id) {
        return territories.get(id);
    }
}
