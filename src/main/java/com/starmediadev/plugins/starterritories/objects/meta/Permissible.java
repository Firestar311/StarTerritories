package com.starmediadev.plugins.starterritories.objects.meta;

import com.starmediadev.plugins.starterritories.objects.role.*;

import java.util.UUID;

public interface Permissible {
    RoleList getRoleList();
    Role getRole(UUID uniqueId);
    void setRole(UUID uniqueId, String roleId);
    default void setRole(UUID uniqueId, Role role) {
        setRole(uniqueId, role.getId());
    }
}
