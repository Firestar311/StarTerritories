package com.starmediadev.plugins.starterritories.objects.territory;

import com.starmediadev.plugins.starmcutils.region.Cuboid;
import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.impl.Property;
import com.starmediadev.utils.Utils;
import org.bukkit.Location;

import java.util.*;

public class TerritoryManager {
    private Map<String, Territory> territories = new HashMap<>();
    
    public void addTerritory(Territory territory) {
        territories.put(territory.getId(), territory);
    }
    
    public Territory getTerritory(String id) {
        return territories.get(id);
    }
    
    public Property createProperty(Owner owner, Cuboid claim) {
        String id = generateId();
        Property property = new Property(id, owner, claim);
        this.territories.put(property.getId(), property);
        return property;
    }
    
    private String generateId() {
        String id;
        do {
            id = Utils.generateCode(12, false, true, false);
        } while (territories.containsKey(id));
        return id;
    }
    
    public Collection<Territory> getTerritories() {
        return new ArrayList<>(territories.values());
    }
    
    public Territory getTerritory(Location location) {
        for (Territory territory : this.territories.values()) {
            if (territory.contains(location)) {
                return territory;
            }
        }
        return null;
    }
}
