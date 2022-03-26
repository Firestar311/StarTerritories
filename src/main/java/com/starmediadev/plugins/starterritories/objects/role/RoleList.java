package com.starmediadev.plugins.starterritories.objects.role;

import java.util.*;

public class RoleList {
    private Role outsiderRole, defaultRole, leaderRole;
    private Map<String, Role> roles = new HashMap<>();
    
    public Role getOutsiderRole() {
        return this.outsiderRole;
    }
    
    public Role getDefaultRole() {
        return this.defaultRole;
    }
    
    public boolean contains(String id) {
        return roles.containsKey(id);
    }
    
    public Role getRole(String roleId) {
        return roles.get(roleId);
    }
    
    public void addRole(Role role) {
        this.roles.put(role.getId(), role);
    }
    
    public void setOutsiderRole(Role outsiderRole) {
        this.outsiderRole = outsiderRole;
    }
    
    public void setDefaultRole(Role defaultRole) {
        this.defaultRole = defaultRole;
    }
    
    public void setLeaderRole(Role leaderRole) {
        this.leaderRole = leaderRole;
    }
    
    public Role getLeaderRole() {
        return leaderRole;
    }
}
