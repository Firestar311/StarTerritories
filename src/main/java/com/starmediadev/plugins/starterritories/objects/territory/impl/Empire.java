package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starterritories.objects.exceptions.AddSubTerritoryException;
import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.*;

public class Empire extends Nation {
    protected int maxKingdoms;
    public Empire(String id, Owner owner) {
        super(id, owner, 300, 1500, 200);
        maxKingdoms = 50;
    }
    
    @Override
    public void addTerritory(Territory territory) {
        if (!(territory instanceof Property) || !(territory instanceof Municipality) || !(territory instanceof Kingdom)) {
            throw new AddSubTerritoryException(this, territory);
        }
        this.territories.put(territory.getId(), territory);
    }
}
