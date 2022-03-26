package com.starmediadev.plugins.starterritories.objects.flag;

import com.starmediadev.plugins.starterritories.StarTerritories;
import org.bukkit.entity.Player;

public class Flag {
    protected String name;
    protected FlagValue value = FlagValue.UNDEFINED;
    protected boolean supportsRestricted = false, requiresPlayer;
    
    protected Flag(String name, boolean supportsRestricted, boolean requiresPlayer) {
        this.name = name;
        this.supportsRestricted = supportsRestricted;
        this.requiresPlayer = requiresPlayer;
    }
    
    public Flag(String name, boolean requiresPlayer) {
        this.name = name;
        this.requiresPlayer = requiresPlayer;
    }
    
    public String getName() {
        return this.name;
    }
    
    public FlagValue getValue() {
        return value;
    }
    
    public FlagValue getEffectiveValue(Player player, Object object) {
        if (supportsRestricted && this.value == FlagValue.RESTRICTED) {
            FlagValue value = checkRestricted(player, object);
            if (value == FlagValue.RESTRICTED) {
                StarTerritories.getPlugin(StarTerritories.class).getLogger().warning("Flag " + getName() + " is a flag that supports restricted values, but returns the restricted Flag Value, this is an error");
                return value;
            }
        }
        
        return value;
    }
    
    protected FlagValue checkRestricted(Player player, Object object) {
        return this.value;
    }
    
    public boolean supportsRestricted() {
        return supportsRestricted;
    }
    
    public void setValue(FlagValue value) {
        if (!supportsRestricted) {
            if (value == FlagValue.RESTRICTED) {
                throw new IllegalArgumentException("Flag " + name + " does not support restricted values.");
            }
        }
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Flag{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", supportsRestricted=" + supportsRestricted +
                '}';
    }
}
