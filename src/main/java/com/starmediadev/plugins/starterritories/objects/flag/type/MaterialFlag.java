package com.starmediadev.plugins.starterritories.objects.flag.type;

import com.starmediadev.plugins.starterritories.objects.flag.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MaterialFlag extends TypeFlag<Material> {
    
    public MaterialFlag(String name, boolean requiresPlayer, FlagValue defaultValue) {
        super(name, requiresPlayer, defaultValue);
    }
    
    @Override
    protected boolean isValidType(Object object) {
        return object instanceof Material;
    }
    
    @Override
    protected FlagValue checkRestricted(Player player, Object object) {
        if (this.value == FlagValue.RESTRICTED) {
            if (object instanceof Material material) {
                return types.getOrDefault(material, this.defaultTypeValue);
            }
        }
        
        return this.value;
    }
}
