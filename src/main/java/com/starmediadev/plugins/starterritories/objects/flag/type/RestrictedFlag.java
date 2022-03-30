package com.starmediadev.plugins.starterritories.objects.flag.type;

import com.starmediadev.plugins.starterritories.objects.flag.*;

public abstract class RestrictedFlag extends Flag {
    protected FlagValue defaultTypeValue;
    
    public RestrictedFlag(String name, boolean requiresPlayer, FlagValue defaultTypeValue) {
        super(name, true, requiresPlayer);
        this.defaultTypeValue = defaultTypeValue;
    }
    
    public FlagValue getDefaultTypeValue() {
        return defaultTypeValue;
    }
    
    public void setDefaultTypeValue(FlagValue defaultTypeValue) {
        this.defaultTypeValue = defaultTypeValue;
    }
    
    public abstract boolean isValidType(Object object);
}
