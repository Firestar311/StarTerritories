package com.starmediadev.plugins.starterritories.objects.meta;

import com.starmediadev.plugins.starterritories.objects.exceptions.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;

import java.util.UUID;

public interface Plottable {
    void claimPlot(UUID actor, Plot plot) throws PlotException;
    boolean isClaimedPlot(Plot plot);
    void unclaimPlot(Plot plot) throws PlotException;
}
