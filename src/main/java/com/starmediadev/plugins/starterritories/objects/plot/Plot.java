package com.starmediadev.plugins.starterritories.objects.plot;

import com.starmediadev.plugins.starmcutils.region.Cuboid;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import com.starmediadev.plugins.starterritories.objects.owner.*;
import com.starmediadev.plugins.starterritories.objects.plot.data.*;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public final class Plot extends Cuboid implements Ownable, Flagable {
    
    private final String id;
    private PlotData plotData;
    private Owner owner;
    private ClaimInfo claimInfo;
    
    public Plot(String id, Chunk chunk, PlotData plotData) {
        super(chunk.getBlock(0, chunk.getWorld().getMinHeight(), 0).getLocation(), chunk.getBlock(15, chunk.getWorld().getMaxHeight(), 0).getLocation());
        this.id = id;
        this.plotData = plotData;
        this.plotData.setPlot(this);
    }
    
    public Plot(String id, PlotData plotData, String worldName, int xMin, int xMax, int zMin, int zMax) {
        super(worldName, xMin, Bukkit.getWorld(worldName).getMinHeight(), zMin, xMax, Bukkit.getWorld(worldName).getMaxHeight(), zMax);
        this.id = id;
        this.plotData = plotData;
    }
    
    public void claim(Owner owner, PlotData plotData, long date, UUID actor) {
        setOwner(owner);
        setPlotData(plotData);
        setClaimInfo(new ClaimInfo(this, date, actor));
    }
    
    public ClaimInfo getClaimInfo() {
        return claimInfo;
    }
    
    public void setClaimInfo(ClaimInfo claimInfo) {
        this.claimInfo = claimInfo;
    }
    
    public String getId() {
        return id;
    }
    
    public PlotData getPlotData() {
        return plotData;
    }
    
    public void setPlotData(PlotData plotData) {
        if (plotData != null) {
            this.plotData = plotData;
        }
    }
    
    @Override
    public void setFlag(Flags flag, FlagValue value) {
        this.plotData.setFlag(flag, value);
    }
    
    @Override
    public FlagList getFlags() {
        return plotData.getFlags();
    }
    
    @Override
    public FlagValue getFlagValue(Flags flag, Player player, Location location, Object object) {
        return this.plotData.getFlagValue(flag, player, location, object);
    }
    
    @Override
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    @Override
    public Owner getOwner() {
        return owner;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Plot plot = (Plot) o;
        return Objects.equals(id, plot.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
