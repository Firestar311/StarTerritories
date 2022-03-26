package com.starmediadev.plugins.starterritories.objects.territory;

import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.exceptions.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import com.starmediadev.plugins.starterritories.objects.owner.*;
import com.starmediadev.plugins.starterritories.objects.plot.*;
import com.starmediadev.plugins.starterritories.objects.plot.data.*;
import com.starmediadev.plugins.starterritories.objects.resident.*;
import com.starmediadev.plugins.starterritories.objects.role.*;
import com.starmediadev.plugins.starterritories.objects.territory.impl.Property;
import org.bukkit.Location;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public abstract class Municipality extends Territory implements Nameable, Plottable, Permissible {
    
    protected String name;
    protected RoleList roleList = new RoleList();
    protected PlotList plotList = new PlotList();
    protected Map<String, Territory> territories = new HashMap<>();
    protected int maxPlots, maxResidents;
    
    public Municipality(String id, Owner owner, int maxPlots, int maxResidents) {
        super(id, owner);
        this.maxPlots = maxPlots;
        this.maxResidents = maxResidents;
    }
    
    public void addTerritory(Territory territory) {
        if (!(territory instanceof Property)) {
            throw new AddSubTerritoryException(this, territory);
        }
        this.territories.put(territory.getId(), territory);
    }
    
    public Territory getTerritory(String id) {
        return territories.get(id);
    }
    
    @Override
    public boolean isResident(UUID uuid) {
        Resident resident = StarTerritories.getPlugin(StarTerritories.class).getResidentManager().get(uuid);
        Role role = resident.getTerritoryRole(this);
        return !roleList.getOutsiderRole().getId().equals(role.getId());
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public RoleList getRoleList() {
        return roleList;
    }
    
    @Override
    public Role getRole(UUID uniqueId) {
        Resident resident = StarTerritories.getPlugin(StarTerritories.class).getResidentManager().get(uniqueId);
        if (resident != null) {
            Role role = resident.getTerritoryRole(this);
            if (role == null || !roleList.contains(role.getId())) {
                return roleList.getDefaultRole();
            }
        }
        return roleList.getOutsiderRole();
    }
    
    @Override
    public void setRole(UUID uniqueId, String roleId) {
        Resident resident = StarTerritories.getPlugin(StarTerritories.class).getResidentManager().get(uniqueId);
        if (resident != null) {
            TerritoryMembership membership = resident.getTerritoryMembership(this);
            if (membership == null) {
                resident.addMembership(new TerritoryMembership(this.getId(), roleList.getRole(roleId), System.currentTimeMillis()));
            }
        }
    }
    
    @Override
    public void claimPlot(UUID actor, Plot plot) throws PlotException {
        if (plot.getOwner() != null) {
            throw new PlotAlreadyClaimedException(plot);
        }
    
        plot.claim(this.owner, new NormalPlotData(plot), System.currentTimeMillis(), actor);
        this.plotList.add(plot);
    }
    
    @Override
    public boolean isClaimedPlot(Plot plot) {
        return this.plotList.contains(plot);
    }
    
    @Override
    public void unclaimPlot(Plot plot) throws PlotException {
        if (plot.getOwner() == null) {
            throw new PlotNotClaimedException(plot, this);
        }
        
        if (plot.getOwner() instanceof TerritoryOwner owner) {
            if (!owner.getIdentifier().equalsIgnoreCase(this.getId())) {
                throw new PlotNotClaimedException(plot, this);
            }
            
            this.plotList.remove(plot);
            plot.setOwner(null);
            plot.setPlotData(new WildernessPlotData(plot));
        } else {
            throw new PlotNotClaimedException(plot, this);
        }
    }
    
    @Override
    public boolean contains(Location location) {
        for (Territory territory : this.territories.values()) {
            if (territory.contains(location)) {
                return true;
            }
        }
        for (Plot plot : this.plotList.getPlots()) {
            if (plot.contains(location)) {
                return true;
            }
        }
        return false;
    }
}
