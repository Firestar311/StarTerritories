package com.starmediadev.plugins.starterritories.objects.owner;

import org.bukkit.Bukkit;

public final class ServerOwner extends Owner {
    public ServerOwner() {
        super("console");
    }
    
    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
    
    @Override
    public String getName() {
        return "Console";
    }
    
    @Override
    public String serialize() {
        return getClass().getName();
    }
}
