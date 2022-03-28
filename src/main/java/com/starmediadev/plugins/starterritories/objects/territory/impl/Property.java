package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starmcutils.region.Cuboid;
import com.starmediadev.plugins.starterritories.objects.owner.*;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import org.bukkit.Location;

import java.util.UUID;

public class Property extends Territory {
    
    private Cuboid claim;
    
    public Property(String id, Owner owner, Cuboid claim) {
        super(id, owner);
        this.claim = claim;
    }
    
    public Cuboid getClaim() {
        return claim;
    }
    
    public void setClaim(Cuboid claim) {
        this.claim = claim;
    }
    
    @Override
    public boolean isResident(UUID uuid) {
        if (getOwner() instanceof PlayerOwner playerOwner) {
            return playerOwner.getUuid().equals(uuid);
        }
        return false;
    }
    
    @Override
    public boolean contains(Location location) {
        return claim.contains(location);
    }
}
