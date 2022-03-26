package com.starmediadev.plugins.starterritories.objects.role;

import com.starmediadev.plugins.starterritories.objects.flag.*;

import java.util.*;

public class Role {
    protected String id, name;
    protected Set<Permission> permissions = new HashSet<>();
    protected int weight;
    protected Map<Flags, FlagValue> flagOverride = new HashMap<>();
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public int getWeight() {
        return weight;
    }
}
