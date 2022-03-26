package com.starmediadev.plugins.starterritories.objects.exceptions;

import com.starmediadev.plugins.starterritories.objects.plot.Plot;

public class PlotAlreadyClaimedException extends PlotException {
    public PlotAlreadyClaimedException(Plot plot) {
        super(plot);
    }
}
