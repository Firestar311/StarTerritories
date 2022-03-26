package com.starmediadev.plugins.starterritories.listener;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.meta.Permissible;
import com.starmediadev.plugins.starterritories.objects.owner.TerritoryOwner;
import com.starmediadev.plugins.starterritories.objects.plot.*;
import com.starmediadev.plugins.starterritories.objects.role.Role;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

import static com.starmediadev.plugins.starterritories.StarTerritories.*;

public class FlagListener implements Listener {
    private StarTerritories plugin;
    
    public FlagListener(StarTerritories plugin) {
        this.plugin = plugin;
//        
//        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                Location lastLocation = lastPlayerLocations.get(player.getUniqueId());
//                if (lastLocation == null) {
//                    lastLocation = player.getLocation();
//                }
//                Location location = player.getLocation();
//                if (location.getWorld().getName().equals(lastLocation.getWorld().getName()) && location.getBlockX() == lastLocation.getBlockX() &&
//                        location.getBlockY() == lastLocation.getBlockY() && location.getBlockZ() == lastLocation.getBlockZ()) {
//                    continue;
//                }
//                
//                Plot lastPlot = plugin.getPlotManager().getPlot(lastLocation), currentPlot = plugin.getPlotManager().getPlot(location);
//                if (lastPlot.equals(currentPlot)) {
//                    continue;
//                }
//                
//                if (currentPlot.getFlagValue(Flags.ENTRY, player, getRole(currentPlot, player)) == FlagValue.DENY) {
//                    player.teleport(lastLocation);
//                } else if (lastPlot.getFlagValue(Flags.EXIT, player, getRole(lastPlot, player)) == FlagValue.DENY) {
//                    player.teleport(lastLocation);
//                }
//    
//                this.lastPlayerLocations.put(player.getUniqueId(), location);
//            }
//        }, 1L, 1L);
    }
    
    private void handleSingleLocationEvent(Cancellable cancellable, Flags flag, Location location, Player player, Object object) {
        Plot plot = plugin.getPlotManager().getPlot(location);
        FlagValue flagValue = plot.getFlagValue(flag, player, object);
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + flag.name());
            cancellable.setCancelled(true);
        }
    }
    
    private Role getRole(Plot plot, Player player) {
        if (plot.getOwner() instanceof TerritoryOwner territoryOwner) {
            Territory territory = territoryOwner.getTerritory();
            if (territory instanceof Permissible permissible) {
                return permissible.getRole(player.getUniqueId());
            }
        }
        
        return null;
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        Plot plot = plugin.getPlotManager().getPlot(e.getLocation());
        LivingEntity entity = e.getEntity();
        if (PASSIVE_MOBS.contains(entity.getType())) {
            if (plot.getFlagValue(Flags.PASSIVE_MOB_SPAWN, null, entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.PASSIVE_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (NEUTRAL_MOBS.contains(entity.getType())) {
            if (plot.getFlagValue(Flags.NEUTRAL_MOB_SPAWN, null, entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.NEUTRAL_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (HOSTILE_MOBS.contains(entity.getType())) {
            if (plot.getFlagValue(Flags.HOSTILE_MOB_SPAWN, null, entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.HOSTILE_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (BOSS_MOBS.contains(entity.getType())) {
            if (plot.getFlagValue(Flags.BOSS_MOB_SPAWN, null, entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.BOSS_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        PlotManager plotManager = plugin.getPlotManager();
        if (e.getEntity() instanceof Player target && e.getDamager() instanceof Player attacker) {
            FlagValue flagValue;
            Plot targetPlot = plotManager.getPlot(target.getLocation()), attackerPlot = plotManager.getPlot(attacker.getLocation());
            if (targetPlot.equals(attackerPlot)) {
                flagValue = targetPlot.getFlagValue(Flags.PVP, null, null);
            } else {
                FlagValue targetValue = targetPlot.getFlagValue(Flags.PVP, target, attacker), attackerValue = attackerPlot.getFlagValue(Flags.PVP, target, attacker);
                if (targetValue == attackerValue) {
                    flagValue = targetValue;
                } else {
                    flagValue = FlagValue.getPriorityValue(targetValue, attackerValue);
                }
            }
            
            if (flagValue == FlagValue.DENY) {
                e.setCancelled(true);
                attacker.sendMessage(MCUtils.color("&cYou are not allowed to PVP here."));
            }
        }
    }
    
    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        PlotManager plotManager = plugin.getPlotManager();
        FlagValue flagValue = FlagValue.UNDEFINED;
        if (e.getEntity() != null) {
            flagValue = plotManager.getPlot(e.getEntity().getLocation()).getFlagValue(Flags.PORTAL_CREATION, null, e.getEntity().getType());
        }
        if (flagValue != FlagValue.DENY) {
            for (BlockState block : e.getBlocks()) {
                Plot plot = plotManager.getPlot(block.getLocation());
                flagValue = plot.getFlagValue(Flags.PORTAL_CREATION, null, e.getEntity());
                if (flagValue == FlagValue.DENY) {
                    break;
                }
            }
        }
        
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented portal creation");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPortalTeleport(PlayerTeleportEvent e) {
        FlagValue flagValue = FlagValue.UNDEFINED;
        
        PlotManager plotManager = plugin.getPlotManager();
        Plot fromPlot = plotManager.getPlot(e.getFrom()), toPlot = plotManager.getPlot(e.getTo());
        if (e.getCause() == TeleportCause.END_PORTAL || e.getCause() == TeleportCause.NETHER_PORTAL || e.getCause() == TeleportCause.END_GATEWAY) {
            if (fromPlot.equals(toPlot)) {
                flagValue = fromPlot.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromPlot.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getCause()), toPlot.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getCause()));
            }
        } else if (e.getCause() == TeleportCause.CHORUS_FRUIT) {
            if (fromPlot.equals(toPlot)) {
                flagValue = fromPlot.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromPlot.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getCause()), toPlot.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getCause()));
            }
        } else if (e.getCause() == TeleportCause.ENDER_PEARL) {
            if (fromPlot.equals(toPlot)) {
                flagValue = fromPlot.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromPlot.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getCause()), toPlot.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getCause()));
            }
        }
        
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + e.getCause() + " Teleport");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        Plot plot = plugin.getPlotManager().getPlot(e.getBlock().getLocation());
        FlagValue flagValue;
        if (e.getCause() == IgniteCause.SPREAD) {
            flagValue = plot.getFlagValue(Flags.FIRESPREAD, e.getPlayer(), e.getCause());
        } else {
            flagValue = plot.getFlagValue(Flags.BLOCK_IGNITE, e.getPlayer(), e.getCause());
        }
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + e.getCause() + " ignite");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        if (e.getSource().getType() == Material.FIRE) {
            handleSingleLocationEvent(e, Flags.FIRESPREAD, e.getBlock().getLocation(), null, Material.FIRE);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        FlagValue flagValue = FlagValue.UNDEFINED;
        if (e.getAction() == Action.PHYSICAL) {
            Block block = e.getClickedBlock();
            if (block != null) {
                Plot plot = plugin.getPlotManager().getPlot(block.getLocation());
                if (block.getType() == Material.FARMLAND) {
                    flagValue = plot.getFlagValue(Flags.CROP_TRAMPLE, e.getPlayer(), e.getClickedBlock().getType());
                } else if (block.getBlockData() instanceof Powerable) {
                    if (block.getType().name().contains("PRESSURE_PLATE")) {
                        flagValue = plot.getFlagValue(Flags.PRESSURE_PLATE, e.getPlayer(), e.getClickedBlock().getType());
                    }
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if (block != null) {
                Plot plot = plugin.getPlotManager().getPlot(block.getLocation());
                if (block.getBlockData() instanceof Door) {
                    flagValue = plot.getFlagValue(Flags.DOOR, e.getPlayer(), block.getType());
                } else if (block.getBlockData() instanceof Switch) {
                    flagValue = plot.getFlagValue(Flags.SWITCH, e.getPlayer(), block.getType());
                } else if (block.getState() instanceof Container) {
                    flagValue = plot.getFlagValue(Flags.CONTAINER, e.getPlayer(), block.getType());
                } else if (block.getBlockData() instanceof Bed && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    flagValue = plot.getFlagValue(Flags.SLEEP, e.getPlayer(), block.getType());
                } else {
                    flagValue = plot.getFlagValue(Flags.BLOCK_INTERACT, e.getPlayer(), block.getType());
                }
            }
        }
        
        if (flagValue == FlagValue.DENY) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player player) {
            handleSingleLocationEvent(e, Flags.ITEM_PICKUP, player.getLocation(), player, e.getItem().getItemStack().getType());
        }
    }
    
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Plot plot = plugin.getPlotManager().getPlot(e.getPlayer().getLocation());
        if (e.getItem().getType() == Material.POTION || e.getItem().getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) e.getItem().getItemMeta();
            FlagValue baseValue = plot.getFlagValue(Flags.POTION_CONSUME, e.getPlayer(), potionMeta.getBasePotionData().getType().getEffectType());
            if (baseValue == FlagValue.DENY) {
                e.setCancelled(true);
                return;
            }
            
            for (PotionEffect customEffect : potionMeta.getCustomEffects()) {
                FlagValue customValue = plot.getFlagValue(Flags.POTION_CONSUME, e.getPlayer(), customEffect.getType());
                if (customValue == FlagValue.DENY) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        Flags flag = switch (e.getBlock().getType()) {
            case WATER, BUBBLE_COLUMN -> Flags.WATER_FLOW;
            case LAVA -> Flags.LAVA_FLOW;
            case DRAGON_EGG -> Flags.DRAGON_EGG_TELEPORT;
            default ->
                    null; //Should never be this as this event only fires on liquids and the dragon egg, this just satisfies the switch statement
        };
        
        if (flag == null) {
            System.out.println(e.getBlock().getType());
            return;
        }
        
        FlagValue flagValue;
        Plot fromPlot = plugin.getPlotManager().getPlot(e.getBlock().getLocation()), toPlot = plugin.getPlotManager().getPlot(e.getToBlock().getLocation());
        if (fromPlot.equals(toPlot)) {
            flagValue = fromPlot.getFlagValue(flag, null, e.getBlock().getType());
        } else {
            FlagValue fromValue = fromPlot.getFlagValue(flag, null, e.getBlock().getType()), toValue = toPlot.getFlagValue(flag, null, e.getBlock().getType());
            flagValue = FlagValue.getPriorityValue(fromValue, toValue);
        }
        
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + e.getBlock().getType() + " from teleporting/flowing");
            e.setCancelled(true);
        }
    }
    
    private void handleExplode(List<Block> blocks, Object object) {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Plot plot = plugin.getPlotManager().getPlot(block.getLocation());
            FlagValue flagValue = plot.getFlagValue(Flags.EXPLOSION_BLOCK_DAMAGE, null, object);
            if (flagValue == FlagValue.DENY) {
                plugin.getLogger().info("Prevented a block from breaking from an explosion");
                iterator.remove();
            }
        }
    }
    
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        handleExplode(e.blockList(), e.getBlock());
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        handleExplode(e.blockList(), e.getEntity());
    }
    
    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        handleSingleLocationEvent(e, Flags.FOOD_CHANGE, player.getLocation(), player, e.getItem().getType());
    }
    
    @EventHandler
    public void onItemMerge(ItemMergeEvent e) {
        Plot originalPlot = plugin.getPlotManager().getPlot(e.getEntity().getLocation()), targetPlot = plugin.getPlotManager().getPlot(e.getTarget().getLocation());
        FlagValue flagValue = FlagValue.getPriorityValue(originalPlot.getFlagValue(Flags.ITEM_MERGE, null, e.getEntity()), targetPlot.getFlagValue(Flags.ITEM_MERGE, null, e.getTarget()));
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented item merge");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        handleSingleLocationEvent(e, Flags.BLOCK_BREAK, e.getBlock().getLocation(), e.getPlayer(), e.getBlock().getType());
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        handleSingleLocationEvent(e, Flags.BLOCK_PLACE, e.getBlock().getLocation(), e.getPlayer(), e.getBlock().getType());
    }
    
    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        handleSingleLocationEvent(e, Flags.BLOCK_BURNING, e.getBlock().getLocation(), null, e.getBlock().getType());
    }
    
    private void handlePotionEvent(Cancellable cancellable, ThrownPotion entity) {
        Plot plot = plugin.getPlotManager().getPlot(entity.getLocation());
        ItemStack itemStack = entity.getItem();
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        Player thrower = null;
        if (entity.getShooter() instanceof Player player) {
            thrower = player;
        }
        FlagValue flagValue = plot.getFlagValue(Flags.POTION_SPLASH, thrower, potionMeta.getBasePotionData().getType().getEffectType());
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented potion thrown");
            cancellable.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        handlePotionEvent(e, e.getEntity());
    }
    
    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent e) {
        handlePotionEvent(e, e.getEntity());
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        handleSingleLocationEvent(e, Flags.ITEM_DROP, e.getItemDrop().getLocation(), e.getPlayer(), e.getItemDrop().getItemStack().getType());
    }
    
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        handleSingleLocationEvent(e, Flags.CRAFTING, e.getWhoClicked().getLocation(), (Player) e.getWhoClicked(), e.getCurrentItem().getType());
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Plot plot = plugin.getPlotManager().getPlot(e.getEntity().getLocation());
        Flags flag = switch (e.getCause()) {
            case CONTACT, CUSTOM -> Flags.CONTACT_DAMAGE;
            case ENTITY_ATTACK -> Flags.ENTITY_ATTACK_DAMAGE;
            case ENTITY_SWEEP_ATTACK -> Flags.ENTITY_SWEEP_DAMAGE;
            case PROJECTILE -> Flags.PROJECTILE_DAMAGE;
            case SUFFOCATION -> Flags.SUFFOCATION_DAMAGE;
            case FALL -> Flags.FALL_DAMAGE;
            case FIRE, FIRE_TICK -> Flags.FIRE_DAMAGE;
            case MELTING -> Flags.MELTING_DAMAGE;
            case LAVA -> Flags.LAVA_DAMAGE;
            case DROWNING -> Flags.DROWNING_DAMAGE;
            case BLOCK_EXPLOSION -> Flags.EXPLOSION_BLOCK_DAMAGE;
            case ENTITY_EXPLOSION -> Flags.ENTITY_EXPLOSION_DAMAGE;
            case VOID -> Flags.VOID_DAMAGE;
            case LIGHTNING -> Flags.LIGHTNING_DAMAGE;
            case SUICIDE -> Flags.SUICIDE_DAMAGE;
            case STARVATION -> Flags.STARVATION_DAMAGE;
            case POISON -> Flags.POISON_DAMAGE;
            case MAGIC -> Flags.MAGIC_DAMAGE;
            case WITHER -> Flags.WITHER_DAMAGE;
            case FALLING_BLOCK -> Flags.FALLING_BLOCK_DAMAGE;
            case THORNS -> Flags.THORNS_DAMAGE;
            case DRAGON_BREATH -> Flags.DRAGON_BREATH_DAMAGE;
            case FLY_INTO_WALL -> Flags.FLY_INTO_WALL_DAMAGE;
            case HOT_FLOOR -> Flags.HOT_FLOOR_DAMAGE;
            case CRAMMING -> Flags.CRAMMING_DAMAGE;
            case DRYOUT -> Flags.DRY_OUT_DAMAGE;
            case FREEZE -> Flags.FREEZE_DAMAGE;
        };
        
        FlagValue flagValue = plot.getFlagValue(flag, null, e.getEntity().getType());
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented damage type " + e.getCause());
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Plot plot = plugin.getPlotManager().getPlot(e.getEntity().getLocation());
        FlagValue flagValue = plot.getFlagValue(Flags.KEEP_INVENTORY, e.getEntity(), getRole(plot, e.getEntity()));
        World world = e.getEntity().getLocation().getWorld();
        boolean keepInventoryGameRule = world.getGameRuleValue(GameRule.KEEP_INVENTORY);
        if (flagValue == FlagValue.ALLOW && !keepInventoryGameRule) {
            e.setKeepInventory(true);
            e.getDrops().clear();
            plugin.getLogger().info("Allowed player to keep inventory on death while world keep inventory gamerule was false");
        } else if (flagValue == FlagValue.DENY && keepInventoryGameRule) {
            e.setKeepInventory(false);
            for (ItemStack itemStack : e.getEntity().getInventory().getContents()) {
                if (itemStack != null) {
                    e.getDrops().add(itemStack);
                }
            }
            plugin.getLogger().info("Did not allow player to keep inventory on death while world keep inventory gamerule was true");
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        
        Plot fromPlot = plugin.getPlotManager().getPlot(from), toPlot = plugin.getPlotManager().getPlot(to);
        if (fromPlot.equals(toPlot)) {
            return;
        }
        
        if (toPlot.getFlagValue(Flags.ENTRY, e.getPlayer(), getRole(toPlot, e.getPlayer())) == FlagValue.DENY) {
            e.setCancelled(true);
            return;
        }
        
        if (fromPlot.getFlagValue(Flags.EXIT, e.getPlayer(), getRole(fromPlot, e.getPlayer())) == FlagValue.DENY) {
            e.setCancelled(true);
        }
    }
}