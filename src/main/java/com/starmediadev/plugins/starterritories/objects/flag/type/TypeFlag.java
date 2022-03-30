package com.starmediadev.plugins.starterritories.objects.flag.type;

import com.starmediadev.plugins.starterritories.objects.flag.*;

import java.util.*;

public abstract class TypeFlag<T> extends RestrictedFlag {
    
    protected FlagValue defaultTypeValue;
    protected Map<T, FlagValue> types = new HashMap<>();
    
    public TypeFlag(String name, boolean requiresPlayer, FlagValue defaultTypeValue) {
        super(name, requiresPlayer, defaultTypeValue);
        this.defaultTypeValue = defaultTypeValue;
    }
    
    public void addType(Object type, FlagValue value) {
        if (value == FlagValue.ALLOW || value == FlagValue.DENY) {
            this.types.put((T) type, value);
        }
    }
    
    public void setDefaultTypeValue(FlagValue defaultTypeValue) {
        this.defaultTypeValue = defaultTypeValue;
    }
    
    public abstract T convertInput(String input);
    
    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", supportsRestricted=" + supportsRestricted +
                ", defaultTypeValue=" + defaultTypeValue +
                ", types=" + types +
                '}';
    }
}
