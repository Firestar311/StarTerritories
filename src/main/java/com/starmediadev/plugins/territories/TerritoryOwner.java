package com.starmediadev.plugins.territories;

public abstract class TerritoryOwner {
    protected String identifier;

    public TerritoryOwner(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public abstract void sendMessage(String message);
    public abstract String getName();
}
