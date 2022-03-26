package com.starmediadev.plugins.starterritories.objects.meta;

import com.starmediadev.plugins.starterritories.objects.owner.Owner;

public interface Ownable {
    void setOwner(Owner owner);
    Owner getOwner();
}
