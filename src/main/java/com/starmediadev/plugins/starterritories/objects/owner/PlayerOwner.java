package com.starmediadev.plugins.starterritories.objects.owner;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class PlayerOwner extends Owner {
    
    private String name;
    private UUID uuid;
    
    public PlayerOwner(UUID uuid, String name) {
        super(uuid.toString());
        this.uuid = uuid;
        this.name = name;
    }
    
    public PlayerOwner(Player player) {
        this(player.getUniqueId(), player.getName());
    }
    
    public PlayerOwner(OfflinePlayer offlinePlayer) {
        this(offlinePlayer.getUniqueId(), offlinePlayer.getName());
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
    
    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String serialize() {
        return getClass().getName() + ":" + identifier + ":" + this.name;
    }
}
