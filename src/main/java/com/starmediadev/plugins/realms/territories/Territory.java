package com.starmediadev.plugins.realms.territories;

import com.starmediadev.data.annotations.ColumnIgnored;
import com.starmediadev.data.annotations.ColumnInfo;
import com.starmediadev.data.annotations.TableInfo;
import com.starmediadev.data.model.DataInfo;
import com.starmediadev.data.model.IDataObject;
import com.starmediadev.plugins.framework.plot.Plot;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@TableInfo(tableName = "territories")
public class Territory implements IDataObject {
    private DataInfo dataInfo;
    
    @ColumnIgnored
    private Territories plugin;
    
    private String territoryId;
    private TerritoryOwner owner;
    private Location spawnpoint;
    private Set<String> claimedPlots = new HashSet<>();
    
    @ColumnIgnored
    private Map<String, Plot> plotCache = new HashMap<>();

    public DataInfo getDataInfo() {
        return dataInfo;
    }
}
