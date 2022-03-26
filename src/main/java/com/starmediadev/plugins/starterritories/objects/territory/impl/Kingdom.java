package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.Nation;

public class Kingdom extends Nation {
    public Kingdom(String id, Owner owner) {
        super(id, owner, 200, 1000, 100);
    }
}
