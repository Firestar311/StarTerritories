package com.starmediadev.plugins.starterritories.objects.flag;

import com.starmediadev.plugins.starterritories.objects.flag.type.*;

import java.lang.reflect.*;

import static com.starmediadev.plugins.starterritories.objects.flag.FlagValue.*;

public enum Flags {
    NEUTRAL_MOB_SPAWN(false, EntityFlag.class, ALLOW), //WORKS
    HOSTILE_MOB_SPAWN(false, EntityFlag.class, ALLOW), //WORKS
    PASSIVE_MOB_SPAWN(false, EntityFlag.class, ALLOW), //WORKS
    BOSS_MOB_SPAWN(false, EntityFlag.class, ALLOW), //WORKS
    PVP(true), //DONE
    PORTAL_CREATION(false), //works
    PORTAL_TELEPORT(false), //works
    CHORUS_FRUIT(true), //works
    ENDER_PEARL(true), //works
    FIRESPREAD(false, MaterialFlag.class, ALLOW), //works
    BLOCK_IGNITE(false, MaterialFlag.class, ALLOW), //works
    CROP_TRAMPLE(true, MaterialFlag.class, ALLOW), //works
    ITEM_PICKUP(true, MaterialFlag.class, ALLOW), //works
    POTION_CONSUME(true, PotionFlag.class, ALLOW), //works
    LAVA_FLOW(false), //WORKS
    WATER_FLOW(false), //WORKS
    DRAGON_EGG_TELEPORT(false), //WORKS 
    EXPLOSION_BLOCK_DAMAGE(false, MaterialFlag.class, ALLOW), //works
    FOOD_CHANGE(true), //works
    ITEM_MERGE(false, MaterialFlag.class, ALLOW), //works
    BLOCK_BREAK(true, MaterialFlag.class, ALLOW), //works
    BLOCK_PLACE(true, MaterialFlag.class, ALLOW), //works
    ENTRY(true, RoleFlag.class, ALLOW), //DONE
    EXIT(true, RoleFlag.class, ALLOW), //DONE
    BLOCK_BURNING(false, MaterialFlag.class, ALLOW), //works
    POTION_SPLASH(false, PotionFlag.class, ALLOW), //works
    BLOCK_INTERACT(true, MaterialFlag.class, ALLOW), //works
    ITEM_DROP(true, MaterialFlag.class, ALLOW), //works
    CRAFTING(true, MaterialFlag.class, ALLOW), //works
    CONTACT_DAMAGE(false), //works
    ENTITY_ATTACK_DAMAGE(false), //works
    ENTITY_SWEEP_DAMAGE(false), //works
    PROJECTILE_DAMAGE(false), //works
    SUFFOCATION_DAMAGE(false), //works
    FALL_DAMAGE(false), //works
    FIRE_DAMAGE(false), //works
    MELTING_DAMAGE(false), //works
    LAVA_DAMAGE(false), //works
    DROWNING_DAMAGE(false), //works
    ENTITY_EXPLOSION_DAMAGE(false), //works
    VOID_DAMAGE(false), //works
    LIGHTNING_DAMAGE(false), //works
    SUICIDE_DAMAGE(false), //works
    STARVATION_DAMAGE(false), //works
    POISON_DAMAGE(false), //works
    MAGIC_DAMAGE(false), //works
    WITHER_DAMAGE(false), //works
    FALLING_BLOCK_DAMAGE(false), //works
    THORNS_DAMAGE(false), //works
    DRAGON_BREATH_DAMAGE(false), //works
    FLY_INTO_WALL_DAMAGE(false), //works
    HOT_FLOOR_DAMAGE(false), //works
    CRAMMING_DAMAGE(false, EntityFlag.class, ALLOW), //works
    DRY_OUT_DAMAGE(false, EntityFlag.class, ALLOW), //works
    FREEZE_DAMAGE(false, EntityFlag.class, ALLOW), //works
    CONTAINER(true, MaterialFlag.class, ALLOW), //works
    SLEEP(true, RoleFlag.class, ALLOW), //works
    KEEP_INVENTORY(true, RoleFlag.class, ALLOW), //works
    DOOR(true), //works
    SWITCH(true), //works
    PRESSURE_PLATE(true); //DONE
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
