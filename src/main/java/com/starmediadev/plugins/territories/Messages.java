package com.starmediadev.plugins.territories;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    
    private static File file;
    private static FileConfiguration config;
    
    private static Map<String, Message> messages = new HashMap<>();
    
    public static void init(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        
        messages.put("nopermission", new Message("You do not have permission to do that."));
    }
    
    public static String get(String name, String... args) {
        Message message = messages.get(name.toLowerCase());
        if (message != null) {
            return MCUtils.color(message.get(args));
        }
        return "";
    }
}
