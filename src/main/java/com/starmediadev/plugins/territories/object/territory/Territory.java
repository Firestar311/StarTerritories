package com.starmediadev.plugins.territories.object.territory;

import com.starmediadev.plugins.plotframework.Plot;
import com.starmediadev.plugins.territories.Privacy;
import com.starmediadev.plugins.territories.Territories;
import com.starmediadev.plugins.territories.object.owner.TerritoryOwner;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Territory {
    private Territories plugin;
    
    private String territoryId, name;
    private TerritoryOwner owner;
    private Location spawnpoint;
    private Set<String> claimedPlots = new HashSet<>();
    private long createdDate;
    private Set<TerritoryFlag> flags = new HashSet<>();
    private Set<TerritoryMember> members = new HashSet<>();
    private Set<TerritoryInvite> invites = new HashSet<>();
    private Privacy privacy;
    private Set<TerritoryRank> ranks = new HashSet<>();
    
    private Map<String, Plot> plotCache = new HashMap<>();
    
    public Territory(String id, TerritoryOwner owner) {
        this.territoryId = id;
        this.owner = owner;
    }
}
