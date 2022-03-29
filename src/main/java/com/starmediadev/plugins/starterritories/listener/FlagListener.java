package com.starmediadev.plugins.starterritories.listener;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import com.starmediadev.plugins.starterritories.objects.owner.TerritoryOwner;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.plugins.starterritories.objects.role.Role;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import com.starmediadev.plugins.starterritories.objects.territory.impl.Property;
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
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        Flagable flagable;
        Property property = getProperty(e.getLocation());
        if (property != null) {
            flagable = property;
        } else {
            flagable = plugin.getPlotManager().getPlot(e.getLocation());
        }
        LivingEntity entity = e.getEntity();
        if (PASSIVE_MOBS.contains(entity.getType())) {
            if (flagable.getFlagValue(Flags.PASSIVE_MOB_SPAWN, null, entity.getLocation(), entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.PASSIVE_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (NEUTRAL_MOBS.contains(entity.getType())) {
            if (flagable.getFlagValue(Flags.NEUTRAL_MOB_SPAWN, null, entity.getLocation(), entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.NEUTRAL_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (HOSTILE_MOBS.contains(entity.getType())) {
            if (flagable.getFlagValue(Flags.HOSTILE_MOB_SPAWN, null, entity.getLocation(), entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.HOSTILE_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        } else if (BOSS_MOBS.contains(entity.getType())) {
            if (flagable.getFlagValue(Flags.BOSS_MOB_SPAWN, null, entity.getLocation(), entity.getType()) == FlagValue.DENY) {
                plugin.getLogger().info("Flag " + Flags.BOSS_MOB_SPAWN + " was deny");
                e.setCancelled(true);
            }
        }
    }
    
    private Property getProperty(Location location) {
        for (Territory territory : plugin.getTerritoryManager().getTerritories()) {
            if (territory instanceof Property property) {
                if (property.contains(location)) {
                    return property;
                }
            }
        }
        return null;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player target && e.getDamager() instanceof Player attacker) {
            FlagValue flagValue;
            Flagable targetFlagable = getFlagable(e.getEntity().getLocation());
            Flagable attackerFlagable = getFlagable(attacker.getLocation());
            if (targetFlagable.equals(attackerFlagable)) {
                flagValue = targetFlagable.getFlagValue(Flags.PVP, null, target.getLocation(), attacker);
            } else {
                FlagValue targetValue = targetFlagable.getFlagValue(Flags.PVP, target, target.getLocation(), attacker), attackerValue = attackerFlagable.getFlagValue(Flags.PVP, attacker, attacker.getLocation(), target);
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
    
    private Flagable getFlagable(Location location) {
        Flagable flagable = null;
        for (Territory territory : plugin.getTerritoryManager().getTerritories()) {
            if (!(territory instanceof Plottable)) {
                if (territory.contains(location)) {
                    flagable = territory;
                }
            }
        }
        
        if (flagable == null) {
            flagable = plugin.getPlotManager().getPlot(location);
        }
        
        return flagable;
    }
    
    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        FlagValue flagValue = FlagValue.UNDEFINED;
        if (e.getEntity() != null) {
            flagValue = getFlagable(e.getEntity().getLocation()).getFlagValue(Flags.PORTAL_CREATION, null, e.getEntity().getLocation(), null);
        }
        if (flagValue != FlagValue.DENY) {
            for (BlockState block : e.getBlocks()) {
                Flagable flagable = getFlagable(block.getLocation());
                flagValue = flagable.getFlagValue(Flags.PORTAL_CREATION, null, block.getLocation(), block.getType());
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
        
        Flagable fromFlagable = getFlagable(e.getFrom()), toFlagable = getFlagable(e.getTo());
        if (e.getCause() == TeleportCause.END_PORTAL || e.getCause() == TeleportCause.NETHER_PORTAL || e.getCause() == TeleportCause.END_GATEWAY) {
            if (fromFlagable.equals(toFlagable)) {
                flagValue = fromFlagable.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getFrom(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromFlagable.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getFrom(), e.getCause()), toFlagable.getFlagValue(Flags.PORTAL_TELEPORT, e.getPlayer(), e.getTo(), e.getCause()));
            }
        } else if (e.getCause() == TeleportCause.CHORUS_FRUIT) {
            if (fromFlagable.equals(toFlagable)) {
                flagValue = fromFlagable.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getPlayer().getLocation(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromFlagable.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getFrom(), e.getCause()), toFlagable.getFlagValue(Flags.CHORUS_FRUIT, e.getPlayer(), e.getTo(), e.getCause()));
            }
        } else if (e.getCause() == TeleportCause.ENDER_PEARL) {
            if (fromFlagable.equals(toFlagable)) {
                flagValue = fromFlagable.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getPlayer().getLocation(), e.getCause());
            } else {
                flagValue = FlagValue.getPriorityValue(fromFlagable.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getFrom(), e.getCause()), toFlagable.getFlagValue(Flags.ENDER_PEARL, e.getPlayer(), e.getTo(), e.getCause()));
            }
        }
        
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + e.getCause() + " Teleport");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        Flagable flagable = getFlagable(e.getBlock().getLocation());
        FlagValue flagValue;
        if (e.getCause() == IgniteCause.SPREAD) {
            flagValue = flagable.getFlagValue(Flags.FIRESPREAD, e.getPlayer(), e.getBlock().getLocation(), e.getBlock().getType());
        } else {
            flagValue = flagable.getFlagValue(Flags.BLOCK_IGNITE, e.getPlayer(), e.getBlock().getLocation(), e.getBlock().getType());
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
    
    private void handleSingleLocationEvent(Cancellable cancellable, Flags flag, Location location, Player player, Object object) {
        Flagable flagable = getFlagable(location);
        FlagValue flagValue = flagable.getFlagValue(flag, player, location, object);
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + flag.name());
            cancellable.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        FlagValue flagValue = FlagValue.UNDEFINED;
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        
        Flagable flagable = getFlagable(block.getLocation());
        
        if (e.getAction() == Action.PHYSICAL) {
            if (block.getType() == Material.FARMLAND) {
                flagValue = flagable.getFlagValue(Flags.CROP_TRAMPLE, e.getPlayer(), block.getLocation(), block.getType());
            } else if (block.getBlockData() instanceof Powerable) {
                if (block.getType().name().contains("PRESSURE_PLATE")) {
                    flagValue = flagable.getFlagValue(Flags.PRESSURE_PLATE, e.getPlayer(), block.getLocation(), block.getType());
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (block.getBlockData() instanceof Door) {
                flagValue = flagable.getFlagValue(Flags.DOOR, e.getPlayer(), block.getLocation(), block.getType());
            } else if (block.getBlockData() instanceof Switch) {
                flagValue = flagable.getFlagValue(Flags.SWITCH, e.getPlayer(), block.getLocation(), block.getType());
            } else if (block.getState() instanceof Container) {
                flagValue = flagable.getFlagValue(Flags.CONTAINER, e.getPlayer(), block.getLocation(), block.getType());
            } else if (block.getBlockData() instanceof Bed && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                flagValue = flagable.getFlagValue(Flags.SLEEP, e.getPlayer(), block.getLocation(), block.getType());
            } else {
                flagValue = flagable.getFlagValue(Flags.BLOCK_INTERACT, e.getPlayer(), block.getLocation(), block.getType());
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
        Flagable flagable = getFlagable(e.getPlayer().getLocation());
        if (e.getItem().getType() == Material.POTION || e.getItem().getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) e.getItem().getItemMeta();
            FlagValue baseValue = flagable.getFlagValue(Flags.POTION_CONSUME, e.getPlayer(), e.getPlayer().getLocation(), potionMeta.getBasePotionData().getType().getEffectType());
            if (baseValue == FlagValue.DENY) {
                e.setCancelled(true);
                return;
            }
            
            for (PotionEffect customEffect : potionMeta.getCustomEffects()) {
                FlagValue customValue = flagable.getFlagValue(Flags.POTION_CONSUME, e.getPlayer(), e.getPlayer().getLocation(), customEffect.getType());
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
            default -> null; //Should never be this as this event only fires on liquids and the dragon egg, this just satisfies the switch statement
        };
        
        if (flag == null) {
            System.out.println(e.getBlock().getType());
            return;
        }
        
        FlagValue flagValue;
        Flagable fromFlagable = getFlagable(e.getBlock().getLocation()), toFlagable = getFlagable(e.getToBlock().getLocation());
        if (fromFlagable.equals(toFlagable)) {
            flagValue = fromFlagable.getFlagValue(flag, null, e.getBlock().getLocation(), e.getBlock().getType());
        } else {
            FlagValue fromValue = fromFlagable.getFlagValue(flag, null, e.getBlock().getLocation(), e.getBlock().getType()), toValue = toFlagable.getFlagValue(flag, null, e.getToBlock().getLocation(), e.getToBlock().getType());
            flagValue = FlagValue.getPriorityValue(fromValue, toValue);
        }
        
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented " + e.getBlock().getType() + " from teleporting/flowing");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        handleExplode(e.blockList(), e.getBlock());
    }
    
    private void handleExplode(List<Block> blocks, Object object) {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Flagable flagable = getFlagable(block.getLocation());
            FlagValue flagValue = flagable.getFlagValue(Flags.EXPLOSION_BLOCK_DAMAGE, null, block.getLocation(), block.getType());
            if (flagValue == FlagValue.DENY) {
                plugin.getLogger().info("Prevented a block from breaking from an explosion");
                iterator.remove();
            }
        }
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
        Flagable originalFlagable = getFlagable(e.getEntity().getLocation()), targetFlagable = getFlagable(e.getTarget().getLocation());
        FlagValue flagValue = FlagValue.getPriorityValue(originalFlagable.getFlagValue(Flags.ITEM_MERGE, null, e.getEntity().getLocation(), e.getEntity().getItemStack().getType()), targetFlagable.getFlagValue(Flags.ITEM_MERGE, null, e.getTarget().getLocation(), e.getTarget().getItemStack().getType()));
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
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        handlePotionEvent(e, e.getEntity());
    }
    
    private void handlePotionEvent(Cancellable cancellable, ThrownPotion entity) {
        Flagable flagable = getFlagable(entity.getLocation());
        ItemStack itemStack = entity.getItem();
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        Player thrower = null;
        if (entity.getShooter() instanceof Player player) {
            thrower = player;
        }
        FlagValue flagValue = flagable.getFlagValue(Flags.POTION_SPLASH, thrower, entity.getLocation(), potionMeta.getBasePotionData().getType().getEffectType());
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented potion thrown");
            cancellable.setCancelled(true);
        }
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
        Flagable flagable = getFlagable(e.getEntity().getLocation());
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
        
        FlagValue flagValue = flagable.getFlagValue(flag, null, e.getEntity().getLocation(), e.getEntityType());
        if (flagValue == FlagValue.DENY) {
            plugin.getLogger().info("Prevented damage type " + e.getCause());
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Flagable flagable = getFlagable(e.getEntity().getLocation());
        FlagValue flagValue = flagable.getFlagValue(Flags.KEEP_INVENTORY, e.getEntity(), e.getEntity().getLocation(), getRole(flagable, e.getEntity()));
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
    
    private Role getRole(Flagable flagable, Player player) {
        if (flagable instanceof Plot plot) {
            if (plot.getOwner() instanceof TerritoryOwner territoryOwner) {
                Territory territory = territoryOwner.getTerritory();
                if (territory instanceof Permissible permissible) {
                    return permissible.getRole(player.getUniqueId());
                }
            }
        } else {
            return new Role();
        }
        
        return null;
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
        
        if (toPlot.getFlagValue(Flags.ENTRY, e.getPlayer(), from, null) == FlagValue.DENY) {
            e.setCancelled(true);
            return;
        }
        
        if (fromPlot.getFlagValue(Flags.EXIT, e.getPlayer(), to, null) == FlagValue.DENY) {
            e.setCancelled(true);
        }
    }
}