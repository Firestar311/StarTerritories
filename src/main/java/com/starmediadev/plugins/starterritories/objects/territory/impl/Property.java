package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starmcutils.region.Cuboid;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.owner.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    
    @Override
    public FlagValue getFlagValue(Flags flag, Player player, Location location, Object object) {
        FlagValue flagValue = this.flagList.get(flag.name()).getEffectiveValue(player, object);
        if (flagValue == FlagValue.UNDEFINED) {
            Plot plot = StarTerritories.getPlugin(StarTerritories.class).getPlotManager().getPlot(location);
            flagValue = plot.getFlagValue(flag, player, location, object);
        }
        return flagValue;
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
