package com.starmediadev.plugins.starterritories.objects.resident;

import java.util.*;

public class ResidentManager {
    private Map<UUID, Resident> residents = new HashMap<>();
    
    public void add(Resident resident) {
        residents.put(resident.getUniqueId(), resident);
    }
    
    public boolean contains(UUID uuid) {
        return residents.containsKey(uuid);
    }
    
    public Resident get(UUID uuid) {
        return residents.get(uuid);
    }
}
