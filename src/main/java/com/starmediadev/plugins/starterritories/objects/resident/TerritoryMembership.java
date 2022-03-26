package com.starmediadev.plugins.starterritories.objects.resident;

import com.starmediadev.plugins.starterritories.objects.role.Role;

public class TerritoryMembership {
    private final String territoryId;
    private Role role;
    private final long joinDate;
    
    public TerritoryMembership(String territoryId, Role role, long joinDate) {
        this.territoryId = territoryId;
        this.role = role;
        this.joinDate = joinDate;
    }
    
    public String getTerritoryId() {
        return territoryId;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public long getJoinDate() {
        return joinDate;
    }
}
