package com.starmediadev.plugins.starterritories.objects.plot;

import com.starmediadev.plugins.starterritories.objects.plot.data.WildernessPlotData;
import com.starmediadev.utils.Utils;
import org.bukkit.*;

import java.util.*;

public class PlotManager {
    private Map<String, Plot> plotsById = new HashMap<>();
    private Map<Chunk, Plot> plotsByChunk = new HashMap<>();
    
    public void setup() {
        //TODO after some testing is completed
    }
    
    public Plot getPlot(String id) {
        return plotsById.get(id);
    }
    
    public Plot getPlot(Chunk chunk) {
        if (plotsByChunk.containsKey(chunk)) {
            return plotsByChunk.get(chunk);
        }
        
        String id;
        do {
            id = Utils.generateCode(10, false, true, false);
        } while (plotsById.containsKey(id));
        
        Plot plot = new Plot(id, chunk, new WildernessPlotData());
        plotsById.put(plot.getId(), plot);
        plotsByChunk.put(chunk, plot);
        return plot;
    }
    
    public Plot getPlot(Location location) {
        return getPlot(location.getChunk());
    }
    
    public Collection<Plot> getPlots() {
        return plotsById.values();
    }
}
