package com.starmediadev.plugins.starterritories.objects.owner;

import com.starmediadev.plugins.starterritories.objects.meta.Nameable;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;

public final class TerritoryOwner extends Owner{
    
    private Territory territory;
    
    public TerritoryOwner(Territory territory) {
        super(territory.getId());
        this.territory = territory;
    }
    
    public Territory getTerritory() {
        return territory;
    }
    
    @Override
    public void sendMessage(String message) {
        //TODO send message to territory owner/leaders
    }
    
    @Override
    public String getName() {
        if (territory instanceof Nameable nameable) {
            return nameable.getName();
        }
        return territory.getId();
    }
    
    @Override
    public String serialize() {
        return getClass().getName() + ":" + identifier;
    }
}
