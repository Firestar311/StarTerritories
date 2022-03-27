package com.starmediadev.plugins.starterritories.objects.territory;

import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.resident.*;
import com.starmediadev.plugins.starterritories.objects.role.Role;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.UUID;

public abstract class Territory implements Ownable, Flagable {
    protected final String id;
    protected Owner owner;
    protected FlagList flagList = new FlagList();
    
    public Territory(String id, Owner owner) {
        this.id = id;
        this.owner = owner;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public void setFlag(Flags flag, FlagValue value) {
        this.flagList.get(flag.name()).setValue(value);
    }
    
    @Override
    public FlagList getFlags() {
        return flagList;
    }
    
    @Override
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    @Override
    public Owner getOwner() {
        return owner;
    }
    
    @Override
    public FlagValue getFlagValue(Flags flag, Player player, Location location, Object object) {
        return this.flagList.get(flag.name()).getEffectiveValue(player, object);
    }
    
    public void addResident(Resident resident, Role role) {
        resident.addMembership(new TerritoryMembership(getId(), role, System.currentTimeMillis()));
    }
    
    public abstract boolean isResident(UUID uuid);
    
    public boolean contains(Entity entity) {
        return contains(entity.getLocation());
    }
    public boolean contains(Block block) {
        return contains(block.getLocation());
    }
    public abstract boolean contains(Location location);
}
