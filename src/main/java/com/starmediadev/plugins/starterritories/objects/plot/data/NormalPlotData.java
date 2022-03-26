package com.starmediadev.plugins.starterritories.objects.plot.data;

import com.starmediadev.plugins.starterritories.objects.plot.*;

public class NormalPlotData extends PlotData {
    public NormalPlotData(Plot plot) {
        super(plot, PlotType.NORMAL);
    }
    
    public NormalPlotData() {
        super(PlotType.NORMAL);
    }
}
