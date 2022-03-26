package com.starmediadev.plugins.starterritories.objects.owner;

public abstract sealed class Owner permits ServerOwner, PlayerOwner, TerritoryOwner {
    protected final String identifier;
    
    public Owner(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public abstract void sendMessage(String message);
    public abstract String getName();
    public abstract String serialize();
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + identifier;
    }
    
    public static Owner deserialize(String string) {
        return null;
    }
}
