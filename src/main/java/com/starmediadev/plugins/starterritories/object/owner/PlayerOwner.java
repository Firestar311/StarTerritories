package com.starmediadev.plugins.starterritories.object.owner;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public sealed class PlayerOwner extends TerritoryOwner permits AdminOwner {
    
    private UUID uuidCache; //Prevents having to convert from a string all the time
    private String nameCache; //Prevents getting the name all the time
    
    public PlayerOwner(UUID uuid) {
        super(uuid.toString());
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(getUUID());
        if (player != null) {
            player.sendMessage(MCUtils.color(message));
        }
    }
    
    private UUID getUUID() {
        if (uuidCache == null) {
            this.uuidCache = UUID.fromString(getIdentifier());
        }
        
        return uuidCache;
    }

    public String getName() {
        if (nameCache == null || nameCache.equals("")) {
            Player player = Bukkit.getPlayer(getUUID());
            if (player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getUUID());
                if (offlinePlayer == null || offlinePlayer.getName() == null || offlinePlayer.getName().equals("")) {
                    //TODO Get name from mojang api
                } else {
                    nameCache = offlinePlayer.getName();
                }
            } else {
                nameCache = player.getName();
            }
        }
        return nameCache;
    }
}
