package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.Municipality;

public class Town extends Municipality {
    public Town(String id, Owner owner) {
        super(id, owner, 75, 250);
    }
}
