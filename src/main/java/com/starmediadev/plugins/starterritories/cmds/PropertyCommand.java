package com.starmediadev.plugins.starterritories.cmds;

import com.starmediadev.plugins.starmcutils.builder.ItemBuilder;
import com.starmediadev.plugins.starmcutils.command.*;
import com.starmediadev.plugins.starmcutils.region.*;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import com.starmediadev.plugins.starterritories.objects.flag.Flag;
import com.starmediadev.plugins.starterritories.objects.owner.PlayerOwner;
import com.starmediadev.plugins.starterritories.objects.territory.impl.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

public class PropertyCommand extends StarCommand {
    public PropertyCommand(StarTerritories plugin) {
        super("property", "Manage properties", "starterritories.property.command", true, false, new ArrayList<>());
        
        addSubCommand(new SubCommand(this, "tool", "Get the claim tool", "starterritories.property.command.tool") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                ItemStack tool = new ItemBuilder(Material.GOLDEN_SHOVEL).displayName("&eProperty Claim Tool").lore("&7Set the first and second positions", "",
                        "&6&lLeft-Click &fon a block for the first position", "&6&lRight-Click &fon a block to set the second position")
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
                tool = plugin.getNMS().addNBTString(tool, "tooltype", "propertyclaimtool");
                Player player = actor.getPlayer();
                player.getInventory().addItem(tool);
                player.sendMessage(MCUtils.color("&eGiven you the property claim tool."));
            }
        });
        
        addSubCommand(new SubCommand(this, "claim", "Claim the selected area as a Property", "starterritories.property.command.claim") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                Player player = actor.getPlayer();
                Selection selection = plugin.getSelectionManager().getSelection(player);
                if (selection == null) {
                    player.sendMessage(MCUtils.color("&cYou do not have a selection."));
                    return;
                }
    
                if (selection.getPointA() == null || selection.getPointB() == null) {
                    player.sendMessage(MCUtils.color("&cThe selection is not complete, one or both points are not currently selected."));
                    return;
                }
    
                Cuboid cuboid = plugin.getSelectionManager().getCuboid(player);
                if (cuboid == null) {
                    player.sendMessage(MCUtils.color("&cThere was an error creating the area, please report this as a bug."));
                    return;
                }
    
                Property property = plugin.getTerritoryManager().createProperty(new PlayerOwner(player), cuboid);
                player.sendMessage(MCUtils.color("&eSuccessfully created your property with id &b" + property.getId()));
            }
        });
        addSubCommand(new FlagSubCommand<>(this, plugin, Property.class));
    }
}
