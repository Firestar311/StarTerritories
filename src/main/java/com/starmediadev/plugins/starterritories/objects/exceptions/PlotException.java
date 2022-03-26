package com.starmediadev.plugins.starterritories.objects.exceptions;

import com.starmediadev.plugins.starterritories.objects.plot.Plot;

public class PlotException extends Exception {
    private Plot plot;
    public PlotException(Plot plot) {
        this.plot = plot;
    }
    
    public Plot getPlot() {
        return plot;
    }
}
