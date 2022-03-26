package com.starmediadev.plugins.starterritories.objects.exceptions;

import com.starmediadev.plugins.starterritories.objects.territory.Territory;

public class AddSubTerritoryException extends RuntimeException {
    private final Territory parentTerritory, subTerritory;
    
    public AddSubTerritoryException(Territory parentTerritory, Territory subTerritory) {
        this.parentTerritory = parentTerritory;
        this.subTerritory = subTerritory;
    }
    
    public Territory getParentTerritory() {
        return parentTerritory;
    }
    
    public Territory getSubTerritory() {
        return subTerritory;
    }
}
