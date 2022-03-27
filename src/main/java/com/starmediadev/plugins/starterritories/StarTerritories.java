package com.starmediadev.plugins.starterritories;

import com.starmediadev.plugins.starterritories.cmds.PlotCmd;
import com.starmediadev.plugins.starterritories.listener.FlagListener;
import com.starmediadev.plugins.starterritories.objects.plot.PlotManager;
import com.starmediadev.plugins.starterritories.objects.resident.ResidentManager;
import com.starmediadev.plugins.starterritories.objects.territory.TerritoryManager;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

import static org.bukkit.entity.EntityType.*;

public class StarTerritories extends JavaPlugin {
    
    private PlotManager plotManager;
    private TerritoryManager territoryManager;
    private ResidentManager residentManager;
    
    public static final EnumSet<EntityType> PASSIVE_MOBS = EnumSet.of(AXOLOTL, BAT, CAT, CHICKEN, COD, COW, DONKEY, FOX, GLOW_SQUID, HORSE, MUSHROOM_COW, MULE, OCELOT, PARROT, PIG, PUFFERFISH, RABBIT, SALMON, SHEEP, SKELETON_HORSE, SNOWMAN, SQUID, STRIDER, TROPICAL_FISH, TURTLE, VILLAGER, WANDERING_TRADER);
    public static final EnumSet<EntityType> NEUTRAL_MOBS = EnumSet.of(BEE, CAVE_SPIDER, DOLPHIN, ENDERMAN, GOAT, IRON_GOLEM, LLAMA, PANDA, PIGLIN, POLAR_BEAR, SPIDER, TRADER_LLAMA, WOLF, ZOMBIFIED_PIGLIN);
    public static final EnumSet<EntityType> HOSTILE_MOBS = EnumSet.of(BLAZE, CREEPER, DROWNED, ELDER_GUARDIAN, ENDERMITE, EVOKER, GHAST, GUARDIAN, HOGLIN, HUSK, MAGMA_CUBE, PHANTOM, PIGLIN_BRUTE, PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON, SLIME, STRAY, VEX, VINDICATOR, WITCH, WITHER_SKELETON, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER);
    public static final EnumSet<EntityType> BOSS_MOBS = EnumSet.of(WITHER, ENDER_DRAGON);
    
    @Override
    public void onEnable() {
        plotManager = new PlotManager();
        territoryManager = new TerritoryManager();
        residentManager = new ResidentManager();
        getServer().getPluginManager().registerEvents(new FlagListener(this), this);
        PlotCmd plotCmd = new PlotCmd(this);
        getCommand("plot").setExecutor(plotCmd);
        getCommand("plot").setTabCompleter(plotCmd);
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public PlotManager getPlotManager() {
        return plotManager;
    }
    
    public TerritoryManager getTerritoryManager() {
        return territoryManager;
    }
    
    public ResidentManager getResidentManager() {
        return residentManager;
    }
}
