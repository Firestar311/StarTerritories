package com.starmediadev.plugins.territories;

import com.starmediadev.data.properties.SqlProperties;
import com.starmediadev.data.properties.SqliteProperties;
import com.starmediadev.plugins.data.events.DatabaseRegisterEvent;
import com.starmediadev.plugins.data.events.HandlerRegisterEvent;
import com.starmediadev.plugins.data.events.PostDataSetupEvent;
import com.starmediadev.plugins.data.events.TypeRegisterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StarDataListener implements Listener {
    
    private Territories plugin;
    
    @EventHandler
    public void onDatabaseSetup(DatabaseRegisterEvent e) {
        Path dbFile = Path.of(plugin.getDataFolder().toString(), "territories.db");
        if (Files.notExists(dbFile)) {
            try {
                Files.createFile(dbFile);
            } catch (IOException ex) {
                plugin.getLogger().severe("Error while creating the database file");
            }
        }
        SqlProperties sqlProperties = new SqliteProperties().setFile(dbFile);
        e.addDatabaseDetails(sqlProperties);
    }

    @EventHandler
    public void onTypeRegister(TypeRegisterEvent e) {
        e.addDataType(Territory.class);
    }

    @EventHandler
    public void onHandlerRegister(HandlerRegisterEvent e) {
        e.registerTypeHandler(new OwnerTypeHandler());
    }

    @EventHandler
    public void onPostSetup(PostDataSetupEvent e) {
        plugin.setDatabaseManager(e.getDatabaseManager());
    }
    
}
