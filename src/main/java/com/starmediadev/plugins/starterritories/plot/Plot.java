package com.starmediadev.plugins.starterritories.plot;

import com.starmediadev.plugins.starmcutils.region.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Plot {

    private String worldName, plotId;
    private int x, z;

    private Cuboid cuboid;
    private Chunk chunk;
    private World world;

    public Plot(String plotId, String worldName, int x, int z) {
        this.plotId = plotId;
        this.worldName = worldName;
        this.x = x;
        this.z = z;
    }

    public Plot(String plotId, String worldName, Chunk chunk) {
        this(plotId, worldName, chunk.getBlock(0, 0, 0).getX(), chunk.getBlock(0, 0, 0).getZ());
        this.chunk = chunk;
    }

    public String getPlotId() {
        return plotId;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
    
    public boolean contains(Location location) {
        return getCuboid().contains(location);
    }

    public boolean contains(Player player) {
        return getCuboid().contains(player);
    }

    public boolean contains(Entity entity) {
        return getCuboid().contains(entity);
    }

    public boolean contains(Location location, double marge) {
        return getCuboid().contains(location, marge);
    }

    public boolean contains(World world, int x, int y, int z) {
        return getCuboid().contains(world, x, y, z);
    }

    public Cuboid getCuboid() {
        if (cuboid == null) {
            Chunk chunk = getChunk();
            Location min = chunk.getBlock(0, 0, 0).getLocation();
            Location max = chunk.getBlock(15, 255, 15).getLocation();
            this.cuboid = new Cuboid(min, max);
        }

        return cuboid;
    }
    
    public Chunk getChunk() {
        if (this.chunk == null) {
            World world = getWorld();
            this.chunk = world.getChunkAt(x, z);
        }
        return chunk;
    }
    
    public World getWorld() {
        if (world == null) {
            world = Bukkit.getWorld(worldName);
        }
        return world;
    }
}
