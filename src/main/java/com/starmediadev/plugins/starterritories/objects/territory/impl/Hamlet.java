package com.starmediadev.plugins.starterritories.objects.territory.impl;

import com.starmediadev.plugins.starterritories.objects.owner.Owner;
import com.starmediadev.plugins.starterritories.objects.territory.Municipality;

public class Hamlet extends Municipality {
    public Hamlet(String id, Owner owner) {
        super(id, owner, 25, 50);
    }
}
