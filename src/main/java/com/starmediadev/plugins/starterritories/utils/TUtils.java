package com.starmediadev.plugins.starterritories.utils;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.objects.flag.*;
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
    
        if (flagValue == FlagValue.DISABLED || flagValue == FlagValue.RESTRICTED) {
            player.sendMessage("&cOnly allow, undefined and deny are allowed to be set by this command at this time");
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
            flagableId = identifiable.getId(); //Should be the case
        }
    
        if (args[1].equalsIgnoreCase("view")) {
            FlagValue flagValue = flagable.getFlags().get(flag.name()).getValue();
            player.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId));
        } else if (args[1].equalsIgnoreCase("set")) {
            FlagValue flagValue = TUtils.getFlagValueFromInput(args[3], player);
            if (flagValue == null) {
                return;
            }
        
            flagable.setFlag(flag, flagValue);
            player.sendMessage(MCUtils.color("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId));
        }
    }
}
