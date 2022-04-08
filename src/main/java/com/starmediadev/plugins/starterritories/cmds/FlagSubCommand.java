package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.command.*;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.flag.type.*;
import com.starmediadev.plugins.starterritories.objects.meta.*;
import com.starmediadev.plugins.starterritories.objects.plot.Plot;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FlagSubCommand<T extends Flagable> extends SubCommand {
    
    private StarTerritories plugin;
    private Class<T> clazz;
    
    public FlagSubCommand(StarCommand parent, StarTerritories plugin, Class<T> clazz) {
        super(parent, "flag", "Manage the flags", parent.getPermission() + ".flags");
        this.plugin = plugin;
        this.clazz = clazz;
        Argument flagNameArgument = new Argument("flagName", true, "You must provide a flag name.");
        for (Flags flag : Flags.values()) {
            flagNameArgument.getCompletions().add(flag.name().toLowerCase());
        }
    
        SubCommand viewSubCommand = new SubCommand(this, "view", "View information about the flag", "") {
            @Override
            public void handleCommand(StarCommand cmd, CommandActor actor, String[] previousArgs, String label, String[] args) {
                Flags flag = getFlagFromInput(actor, args[0]);
            
                T flagable = getFlagable(actor);
                String flagableName = flagable.getClass().getSimpleName().toLowerCase();
                String flagableId = "";
                if (flagable instanceof Identifiable identifiable) {
                    flagableId = identifiable.getId();
                }
            
                FlagValue flagValue = flagable.getFlags().get(flag.name()).getValue();
                actor.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId));
            }
        };
        viewSubCommand.addArgument(flagNameArgument);
        addSubCommand(viewSubCommand);
    
        //<flagable> flag set <flagName> <flagValue> [type]
        SubCommand setSubCommand = new SubCommand(this, "set", "Sets a flag's value", "") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                Flags flag = getFlagFromInput(actor, args[0]);
    
                T flagable = getFlagable(actor);
                String flagableName = flagable.getClass().getSimpleName().toLowerCase();
                String flagableId = "";
                if (flagable instanceof Identifiable identifiable) {
                    flagableId = identifiable.getId();
                }
                
                FlagValue flagValue = getFlagValueFromInput(actor, args[1]);
                
                if (args.length > 2) {
                    Flag flagInstance = flagable.getFlags().get(flag.name());
                    if (flagInstance == null) {
                        actor.sendMessage("&cThere is a problem with the flag setting, report as a bug");
                        return;
                    }
    
                    if (!flagInstance.supportsRestricted()) {
                        actor.sendMessage("&cThat flag does not support the restricted type, please do not provide additional values.");
                        return;
                    }
    
                    if (flagInstance instanceof RestrictedFlag restrictedFlag) {
                        if (restrictedFlag.getValue() != FlagValue.RESTRICTED) {
                            actor.sendMessage("&cThe flag provided is not set to restricted.");
                            return;
                        }
                        if (!restrictedFlag.isValidType(args[2])) {
                            actor.sendMessage("&cThe value " + args[2] + " is not valid for that flag.");
                            return;
                        }
        
                        if (restrictedFlag instanceof TypeFlag<?> typeFlag) {
                            Object value = null;
                            if (!(typeFlag instanceof RoleFlag)) {
                                value = typeFlag.convertInput(args[2]);
                            } else {
                                if (flagable instanceof Permissible permissible) {
                                    value = permissible.getRoleList().getRole(args[2]);
                                }
                            }
            
                            if (value == null) {
                                actor.sendUncoloredMessage("&cInvalid type provided");
                                return;
                            }
            
                            typeFlag.addType(value, flagValue);
                            actor.sendMessage("&eAdded &b" + value + " &eas an override for the restricted flag &b" + flag.name().toLowerCase() + " &ein " + flagableName + " &b" + flagableId);
                        }
                    }
                } else {
                    flagable.setFlag(flag, flagValue);
                    actor.sendMessage("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein" + flagableName + " &b" + flagableId);
                    if (flagValue == FlagValue.RESTRICTED) {
                        actor.sendMessage("&7&oYou set the value to restricted, please use the set command to set the values.");
                    }
                }
            }
        };
        setSubCommand.addArgument(flagNameArgument);
        Argument flagValueArgment = new Argument("flagValue", true, "You must provide a value for the flag");
        for (FlagValue flagValue : FlagValue.values()) {
            flagValueArgment.getCompletions().add(flagValue.name().toLowerCase());
        }
        setSubCommand.addArgument(flagValueArgment);
        setSubCommand.addArgument(new Argument("type"));
        addSubCommand(setSubCommand);
    }
    
    private Flags getFlagFromInput(CommandActor actor, String input) {
        Flags flag = null;
        try {
            flag = Flags.valueOf(input.toUpperCase());
        } catch (Exception e) {
            actor.sendUncoloredMessage("&cInvalid flag name: " + input);
        }
        return flag;
    }
    
    private T getFlagable(CommandActor actor) {
        Player player = actor.getPlayer();
        Location location = player.getLocation();
        
        if (Plot.class == this.clazz) {
            return (T) plugin.getPlotManager().getPlot(location);
        } else if (Territory.class.isAssignableFrom(this.clazz)) {
            Territory territory = plugin.getTerritoryManager().getTerritory(location);
            if (territory.getClass() == this.clazz) {
                return (T) territory;
            }
        }
        
        return null;
    }
    
    private FlagValue getFlagValueFromInput(CommandActor actor, String input) {
        FlagValue flagValue;
        try {
            flagValue = FlagValue.valueOf(input.toUpperCase());
        } catch (Exception e) {
            actor.sendUncoloredMessage("&cInvalid flag value: " + input);
            return null;
        }
        
        if (flagValue == FlagValue.DISABLED) {
            actor.sendUncoloredMessage("&cOnly allow, undefined, restricted and deny are allowed to be set by this command at this time");
            return null;
        }
        return flagValue;
    }
}
