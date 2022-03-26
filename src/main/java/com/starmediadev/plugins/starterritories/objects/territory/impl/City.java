package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.Municipality;

public class City extends Municipality {
    public City(String id, Owner owner) {
        super(id, owner, 115, 500);
    }
}
