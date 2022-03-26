package com.starmediadev.plugins.starterritories.objects.plot;

import java.util.UUID;

public class ClaimInfo {
    private Plot plot;
    private long claimDate;
    private UUID claimedBy;
    
    public ClaimInfo(Plot plot, long claimDate, UUID claimedBy) {
        this.plot = plot;
        this.claimDate = claimDate;
        this.claimedBy = claimedBy;
    }
    
    public Plot getPlot() {
        return plot;
    }
    
    public long getClaimDate() {
        return claimDate;
    }
    
    public UUID getClaimedBy() {
        return claimedBy;
    }
}
