package com.starmediadev.plugins.starterritories.objects.exceptions;

import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.plugins.starterritories.objects.territory.*;

public class PlotNotClaimedException extends PlotException {
    private final Territory territory;
    
    public PlotNotClaimedException(Plot plot, Territory territory) {
        super(plot);
        this.territory = territory;
    }
    
    public Territory getTerritory() {
        return territory;
    }
}
