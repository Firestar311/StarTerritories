package com.starmediadev.plugins.territories;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;

public class ServerOwner extends TerritoryOwner {
    public ServerOwner() {
        super("SERVER");
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(MCUtils.color(message));
    }

    public String getName() {
        return "Server";
    }
}
