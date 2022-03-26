package com.starmediadev.plugins.starterritories.objects.plot;

import com.starmediadev.plugins.starterritories.StarTerritories;

import java.util.*;

public class PlotList {
    private Set<String> plots = new HashSet<>();
    
    public void add(Plot plot) {
        this.plots.add(plot.getId());
    }
    
    public boolean contains(Plot plot) {
        return plots.contains(plot.getId());
    }
    
    public List<Plot> getPlots() {
        List<Plot> plots = new ArrayList<>();
        for (String plotId : this.plots) {
            Plot plot = StarTerritories.getPlugin(StarTerritories.class).getPlotManager().getPlot(plotId);
            if (plot != null) {
                plots.add(plot);
            }
        }
        return plots;
    }
    
    public void remove(Plot plot) {
        this.plots.remove(plot.getId());
    }
}
