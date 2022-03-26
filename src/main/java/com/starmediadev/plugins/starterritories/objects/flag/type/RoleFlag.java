package com.starmediadev.plugins.starterritories.objects.flag.type;

import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.role.Role;
import org.bukkit.entity.Player;

public class RoleFlag extends TypeFlag<Role> {
    
    public RoleFlag(String name, boolean requiresPlayer, FlagValue defaultTypeValue) {
        super(name, requiresPlayer, defaultTypeValue);
    }
    
    @Override
    protected FlagValue checkRestricted(Player player, Object object) {
        if (this.value == FlagValue.RESTRICTED) {
            if (object instanceof Role role) {
                return types.getOrDefault(role, this.defaultTypeValue);
            }
        }
        
        return this.value;
    }
}
