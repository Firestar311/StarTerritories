package com.starmediadev.plugins.starterritories.listener;

import com.starmediadev.plugins.starmcutils.region.*;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starterritories.StarTerritories;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class ToolListener implements Listener {
    
    private StarTerritories plugin;
    
    private SelectionManager selectionManager;
    
    public ToolListener(StarTerritories plugin) {
        this.plugin = plugin;
        this.selectionManager = plugin.getServer().getServicesManager().getRegistration(SelectionManager.class).getProvider();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        String tooltype = plugin.getNMS().getNBTString(item, "tooltype");
        if (tooltype != null && tooltype.equals("propertyclaimtool")) {
            Block block = e.getClickedBlock();
            int posNumber;
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                selectionManager.setPointA(e.getPlayer(), block.getLocation());
                posNumber = 1;
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                selectionManager.setPointB(e.getPlayer(), block.getLocation());
                posNumber = 2;
            } else {
                return;
            }
            
            int x = block.getX(), y = block.getY(), z = block.getZ();
            e.getPlayer().sendMessage(MCUtils.color("&eSet &bpos" + posNumber + " &eto (&b" + x + "&e, &b" + y + "&e, &b" + z + "&e)"));
            
            e.setCancelled(true);
            if (selectionManager.hasSelection(e.getPlayer())) {
                Selection selection = selectionManager.getSelection(e.getPlayer());
                if (selection.getPointA() != null && selection.getPointB() != null) {
                    e.getPlayer().sendMessage(MCUtils.color("&eYou have both corners set, please use &b/property claim &eto finalize the claim."));
                }
            }
        }
    }
}
