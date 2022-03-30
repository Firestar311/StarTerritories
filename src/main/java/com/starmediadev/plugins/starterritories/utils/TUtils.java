package com.starmediadev.plugins.starterritories.utils;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.flag.type.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import org.bukkit.entity.Player;

public final class TUtils {
    public static Flags getFlagFromInput(String input, Player player) {
        try {
            return Flags.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("&cInvalid flag value: " + input);
            return null;
        }
    }
    
    public static FlagValue getFlagValueFromInput(String input, Player player) {
        FlagValue flagValue;
        try {
            flagValue = FlagValue.valueOf(input.toUpperCase());
        } catch (Exception e) {
            player.sendMessage(MCUtils.color("&cInvalid flag value: " + input));
            return null;
        }
    
        if (flagValue == FlagValue.DISABLED) {
            player.sendMessage("&cOnly allow, undefined, restricted and deny are allowed to be set by this command at this time");
            return null;
        }
        return flagValue;
    }
    
    public static void handleFlagableFlagSubCommand(Player player, String[] args, Flagable flagable) {
        if (!(args.length > 1)) {
            player.sendMessage(MCUtils.color("&cYou must provide a flag sub command."));
            return;
        }
    
        if (!(args.length > 2)) {
            player.sendMessage(MCUtils.color("&cYou must provide a flag name"));
            return;
        }
    
        Flags flag = TUtils.getFlagFromInput(args[2], player);
        if (flag == null) {
            return;
        }
        
        String flagableName = flagable.getClass().getSimpleName().toLowerCase();
        String flagableId = "";
        if (flagable instanceof Identifiable identifiable) {
            flagableId = identifiable.getId();
        }
    
        if (args[1].equalsIgnoreCase("view")) {
            FlagValue flagValue = flagable.getFlags().get(flag.name()).getValue();
            player.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId));
        } else if (args[1].equalsIgnoreCase("set")) {
            FlagValue flagValue = TUtils.getFlagValueFromInput(args[3], player);
            if (flagValue == null) {
                return;
            }
            
            if (args.length >= 4) {
                Flag flagInstance = flagable.getFlags().get(flag.name());
                if (flagInstance == null) {
                    player.sendMessage(MCUtils.color("&cThere is a problem with the flag setting, report as a bug"));
                    return;
                }
                
                if (!flagInstance.supportsRestricted()) {
                    player.sendMessage(MCUtils.color("&cThat flag does not support the restricted type, please do not provide additional values."));
                    return;
                }
                
                if (flagInstance instanceof RestrictedFlag restrictedFlag) {
                    if (restrictedFlag.getValue() != FlagValue.RESTRICTED) {
                        player.sendMessage(MCUtils.color("&cThe flag provided is not set to restricted."));
                        return;
                    }
                    if (!restrictedFlag.isValidType(args[4])) {
                        player.sendMessage(MCUtils.color("&cThe value " + args[4] + " is not valid for that flag."));
                        return;
                    }
                    
                    if (restrictedFlag instanceof TypeFlag<?> typeFlag) {
                        Object value = null;
                        if (!(typeFlag instanceof RoleFlag)) {
                            value = typeFlag.convertInput(args[4]);                            
                        } else {
                            if (flagable instanceof Permissible permissible) {
                                value = permissible.getRoleList().getRole(args[4]);
                            }
                        }
                        
                        if (value == null) {
                            player.sendMessage(MCUtils.color("&cInvalid type provided"));
                            return;
                        }
                        
                        typeFlag.addType(value, flagValue);
                        player.sendMessage(MCUtils.color("&eAdded &b" + value + " &eas an override for the restricted flag &b" + flag.name().toLowerCase() + " &ein " + flagableName + " &b" + flagableId));
                    }
                }
            } else {
                flagable.setFlag(flag, flagValue);
                player.sendMessage(MCUtils.color("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId));
                if (flagValue == FlagValue.RESTRICTED) {
                    player.sendMessage(MCUtils.color("&7&oYou set the value to restricted, please use the set command to set the values."));
                }
            }
        }
    }
}
