package com.starmediadev.plugins.starterritories.objects.flag;

import com.starmediadev.plugins.starterritories.objects.flag.type.*;

import java.lang.reflect.*;

import static com.starmediadev.plugins.starterritories.objects.flag.FlagValue.*;

public enum Flags {
    NEUTRAL_MOB_SPAWN(false, EntityFlag.class, ALLOW),
    HOSTILE_MOB_SPAWN(false, EntityFlag.class, ALLOW), 
    PASSIVE_MOB_SPAWN(false, EntityFlag.class, ALLOW), 
    BOSS_MOB_SPAWN(false, EntityFlag.class, ALLOW), 
    PVP(true),
    PORTAL_CREATION(false), 
    PORTAL_TELEPORT(false), 
    CHORUS_FRUIT(true), 
    ENDER_PEARL(true), 
    FIRESPREAD(false, MaterialFlag.class, ALLOW), 
    BLOCK_IGNITE(false, MaterialFlag.class, ALLOW), 
    CROP_TRAMPLE(true, MaterialFlag.class, ALLOW), 
    ITEM_PICKUP(true, MaterialFlag.class, ALLOW), 
    POTION_CONSUME(true, PotionFlag.class, ALLOW), 
    LAVA_FLOW(false), 
    WATER_FLOW(false), 
    DRAGON_EGG_TELEPORT(false),  
    EXPLOSION_BLOCK_DAMAGE(false, MaterialFlag.class, ALLOW), 
    FOOD_CHANGE(true), 
    ITEM_MERGE(false, MaterialFlag.class, ALLOW), 
    BLOCK_BREAK(true, MaterialFlag.class, ALLOW), 
    BLOCK_PLACE(true, MaterialFlag.class, ALLOW), 
    ENTRY(true, RoleFlag.class, ALLOW),
    EXIT(true, RoleFlag.class, ALLOW),
    BLOCK_BURNING(false, MaterialFlag.class, ALLOW), 
    POTION_SPLASH(false, PotionFlag.class, ALLOW), 
    BLOCK_INTERACT(true, MaterialFlag.class, ALLOW), 
    ITEM_DROP(true, MaterialFlag.class, ALLOW), 
    CRAFTING(true, MaterialFlag.class, ALLOW), 
    CONTACT_DAMAGE(false), 
    ENTITY_ATTACK_DAMAGE(false), 
    ENTITY_SWEEP_DAMAGE(false), 
    PROJECTILE_DAMAGE(false), 
    SUFFOCATION_DAMAGE(false), 
    FALL_DAMAGE(false), 
    FIRE_DAMAGE(false), 
    MELTING_DAMAGE(false), 
    LAVA_DAMAGE(false), 
    DROWNING_DAMAGE(false), 
    ENTITY_EXPLOSION_DAMAGE(false), 
    VOID_DAMAGE(false), 
    LIGHTNING_DAMAGE(false), 
    SUICIDE_DAMAGE(false), 
    STARVATION_DAMAGE(false), 
    POISON_DAMAGE(false), 
    MAGIC_DAMAGE(false), 
    WITHER_DAMAGE(false), 
    FALLING_BLOCK_DAMAGE(false), 
    THORNS_DAMAGE(false), 
    DRAGON_BREATH_DAMAGE(false), 
    FLY_INTO_WALL_DAMAGE(false), 
    HOT_FLOOR_DAMAGE(false), 
    CRAMMING_DAMAGE(false, EntityFlag.class, ALLOW), 
    DRY_OUT_DAMAGE(false, EntityFlag.class, ALLOW), 
    FREEZE_DAMAGE(false, EntityFlag.class, ALLOW), 
    CONTAINER(true, MaterialFlag.class, ALLOW), 
    SLEEP(true, RoleFlag.class, ALLOW), 
    KEEP_INVENTORY(true, RoleFlag.class, ALLOW), 
    DOOR(true), 
    SWITCH(true), 
    PRESSURE_PLATE(true);
    private final FlagValue defaultTypeValue;
    private final boolean supportsRestricted, requiresPlayer;
    private final Class<? extends Flag> flagClass;
    
    Flags(boolean requiresPlayer, Class<? extends Flag> flagClass, FlagValue defaultTypeValue) {
        this.supportsRestricted = true;
        this.requiresPlayer = requiresPlayer;
        this.flagClass = flagClass;
        this.defaultTypeValue = defaultTypeValue;
    }
    
    Flags(boolean requiresPlayer) {
        this.supportsRestricted = false;
        this.flagClass = Flag.class;
        this.defaultTypeValue = null;
        this.requiresPlayer = requiresPlayer;
    }
    
    public FlagValue getDefaultTypeValue() {
        return defaultTypeValue;
    }
    
    public boolean supportsRestricted() {
        return supportsRestricted;
    }
    
    public boolean requiresPlayer() {
        return requiresPlayer;
    }
    
    public Class<? extends Flag> getFlagClass() {
        return flagClass;
    }
    
    public Flag createInstance(FlagValue defaultTypeValue) {
        Constructor<? extends RestrictedFlag> constructor;
        try {
            constructor = (Constructor<? extends RestrictedFlag>) flagClass.getDeclaredConstructor(String.class, boolean.class, FlagValue.class);
        } catch (NoSuchMethodException e ) {
            throw new RuntimeException("Flag " + name() + " does not have the proper constructor.", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Flag " + name() + " has an invalid class defined, should be a sub class of RestrictedFlag");
        }
    
        RestrictedFlag flag;
        try {
            flag = constructor.newInstance(name().toLowerCase(), requiresPlayer, defaultTypeValue);
        } catch (Exception e) {
            throw new RuntimeException("Flag " + name() + " could not be instantiated ", e);
        }
    
        return flag;
    }
    
    public Flag createInstance() {
        if (!supportsRestricted) {
            return new Flag(name().toLowerCase(), requiresPlayer);
        } else {
            return createInstance(UNDEFINED);
        }
    }
}
