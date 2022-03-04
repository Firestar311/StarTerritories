package com.starmediadev.plugins.starterritories.object.owner;

public abstract sealed class TerritoryOwner permits PlayerOwner, ServerOwner {
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
