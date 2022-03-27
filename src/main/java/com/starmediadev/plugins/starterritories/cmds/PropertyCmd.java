package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.builder.ItemBuilder;
import com.starmediadev.plugins.starmcutils.region.*;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.*;
import com.starmediadev.plugins.starterritories.objects.owner.PlayerOwner;
import com.starmediadev.plugins.starterritories.objects.territory.Territory;
import com.starmediadev.plugins.starterritories.objects.territory.impl.Property;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PropertyCmd implements TabExecutor {
    
    private StarTerritories plugin;
    private SelectionManager selectionManager;
    
    public PropertyCmd(StarTerritories plugin) {
        this.plugin = plugin;
        this.selectionManager = plugin.getServer().getServicesManager().getRegistration(SelectionManager.class).getProvider();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players can use that command."));
            return true;
        }
        
        if (!(args.length > 0)) {
            player.sendMessage(MCUtils.color("&cYou must provide a sub command"));
            return true;
        }
        
        if (args[0].equalsIgnoreCase("tool")) {
            ItemStack tool = new ItemBuilder(Material.GOLDEN_SHOVEL).setDisplayName("&eProperty Claim Tool").withLore("&7Set the first and second positions", "",
                    "&6&lLeft-Click &fon a block for the first position", "&6&lRight-Click &fon a block to set the second position").build();
            tool = plugin.getNMS().addNBTString(tool, "tooltype", "propertyclaimtool");
            player.getInventory().addItem(tool);
            player.sendMessage(MCUtils.color("&eGiven you the property claim tool."));
        } else if (args[0].equalsIgnoreCase("claim")) {
            Selection selection = selectionManager.getSelection(player);
            if (selection == null) {
                player.sendMessage(MCUtils.color("&cYou do not have a selection."));
                return true;
            }
            
            if (selection.getPointA() == null || selection.getPointB() == null) {
                player.sendMessage(MCUtils.color("&cThe selection is not complete, one or both points are not currently selected."));
                return true;
            }
            
            Cuboid cuboid = selectionManager.getCuboid(player);
            if (cuboid == null) {
                player.sendMessage(MCUtils.color("&cThere was an error creating the area, please report this as a bug."));
                return true;
            }
            
            Property property = plugin.getTerritoryManager().createProperty(new PlayerOwner(player), cuboid);
            player.sendMessage(MCUtils.color("&eSuccessfully created your property with id &b" + property.getId()));
        } else if (args[0].equalsIgnoreCase("flag")) {
            if (!(args.length > 1)) {
                player.sendMessage(MCUtils.color("&cYou must provide a flag sub command."));
                return true;
            }
            
            if (!(args.length > 2)) {
                player.sendMessage(MCUtils.color("&cYou must provide a flag name"));
                return true;
            }
            
            Flags flag;
            try {
                flag = Flags.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage("&cInvalid flag value: " + args[2]);
                return true;
            }
            
            Territory territory = plugin.getTerritoryManager().getTerritory(player.getLocation());
            if (territory instanceof Property property) {
                if (args[1].equalsIgnoreCase("view")) {
                    FlagValue flagValue = property.getFlags().get(flag.name()).getValue();
                    player.sendMessage(MCUtils.color("&eFlag &b" + flag.name().toLowerCase() + " &ehas the value &b" + flagValue.name().toLowerCase() + " &ein property &b" + property.getId()));
                } else if (args[1].equalsIgnoreCase("set")) {
                    FlagValue flagValue;
                    try {
                        flagValue = FlagValue.valueOf(args[3].toUpperCase());
                    } catch (Exception e) {
                        player.sendMessage(MCUtils.color("&cInvalid flag value: " + args[3]));
                        return true;
                    }
                    
                    if (flagValue == FlagValue.DISABLED || flagValue == FlagValue.RESTRICTED) {
                        player.sendMessage("&cOnly allow, undefined and deny are allowed to be set by this command at this time");
                        return true;
                    }
                    
                    property.setFlag(flag, flagValue);
                    player.sendMessage(MCUtils.color("&eSet the flag &b" + flag.name().toLowerCase() + " &eto &b" + flagValue.name().toLowerCase() + " &ein property &b" + property.getId()));
                }
            } else {
                System.out.println("Territory " + territory.getId() + " is not a property");
            }
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        
        if (args.length == 1) {
            List<String> subCommands = List.of("tool", "claim", "flag");
            List<String> completions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("flag")) {
            List<String> subCommands = List.of("view", "set");
            List<String> completions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[1].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("flag")) {
            List<String> completions = new LinkedList<>();
            for (Flags flag : Flags.values()) {
                if (flag.name().toLowerCase().startsWith(args[2])) {
                    completions.add(flag.name().toLowerCase());
                }
            }
            Collections.sort(completions);
            return completions;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("flag")) {
            List<String> completions = new LinkedList<>();
            for (FlagValue flagValue : FlagValue.values()) {
                if (flagValue.name().toLowerCase().startsWith(args[3])) {
                    completions.add(flagValue.name().toLowerCase());
                }
            }
            
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}
