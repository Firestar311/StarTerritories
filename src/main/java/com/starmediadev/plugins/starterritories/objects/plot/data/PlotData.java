package com.starmediadev.plugins.starterritories.objects.plot.data;

import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.meta.Flagable;
import com.starmediadev.plugins.starterritories.objects.owner.TerritoryOwner;
import com.starmediadev.plugins.starterritories.objects.plot.*;
import org.bukkit.entity.Player;

public abstract class PlotData implements Flagable {
    
    private Plot plot;
    private PlotType plotType;
    private FlagList flagList = new FlagList();
    
    public PlotData(Plot plot, PlotType plotType) {
        this.plot = plot;
        this.plotType = plotType;
    }
    
    public PlotData(PlotType plotType) {
        this.plotType = plotType;
    }
    
    public PlotType getPlotType() {
        return plotType;
    }
    
    @Override
    public void setFlag(Flags flag, FlagValue value) {
        this.flagList.get(flag.name()).setValue(value);
    }
    
    @Override
    public FlagList getFlags() {
        return flagList;
    }
    
    @Override
    public FlagValue getFlagValue(Flags flag, Player player, Object object) {
        FlagValue value = flagList.get(flag.name()).getEffectiveValue(player, object);
        if (value == FlagValue.UNDEFINED) {
            if (this.plot.getOwner() instanceof TerritoryOwner territoryOwner) {
                value = territoryOwner.getTerritory().getFlagValue(flag, player, object);
            }
        }
        
        return value;
    }
    
    public Plot getPlot() {
        return plot;
    }
    
    public void setPlot(Plot plot) {
        this.plot = plot;
    }
}
