package com.starmediadev.plugins.starterritories.objects.meta;

import com.starmediadev.plugins.starterritories.objects.flag.*;
import org.bukkit.entity.Player;

public interface Flagable {
    void setFlag(Flags flag, FlagValue value);
    FlagList getFlags();
    FlagValue getFlagValue(Flags flag, Player player, Object object);
}
