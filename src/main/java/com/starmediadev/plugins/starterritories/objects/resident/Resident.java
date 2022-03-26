package com.starmediadev.plugins.starterritories.objects.resident;

import com.starmediadev.plugins.starterritories.objects.role.Role;
import com.starmediadev.plugins.starterritories.objects.territory.*;

import java.util.*;

public class Resident {
    private final UUID uniqueId;
    private String name;
    private long lastOnline;
    List<TerritoryMembership> territoryMemberships = new ArrayList<>();
    
    public Resident(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getLastOnline() {
        return lastOnline;
    }
    
    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }
    
    public void addMembership(TerritoryMembership territoryMembership) {
        this.territoryMemberships.add(territoryMembership);
    }
    
    public Role getTerritoryRole(Territory territory) {
        for (TerritoryMembership territoryMembership : this.territoryMemberships) {
            return territoryMembership.getRole();
        }
        return null;
    }
    
    public TerritoryMembership getTerritoryMembership(Territory territory) {
        for (TerritoryMembership territoryMembership : this.territoryMemberships) {
            if (territoryMembership.getTerritoryId().equalsIgnoreCase(territory.getId())) {
                return territoryMembership;
            }
        }
        
        return null;
    }
}
