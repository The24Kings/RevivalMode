package com.revive.listeners;

import com.revive.RevivalMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.slf4j.Logger;

public class ChestClose implements Listener {
    private static final RevivalMode plugin = RevivalMode.getPlugin();
    private final Logger logger = plugin.getSLF4JLogger();

    // TODO: Add a hologram with the text "<Player>'s Grave"
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Location location = inv.getLocation();

        if(!inv.isEmpty()) {
            return;
        }

        if (!RevivalMode.chests.contains(location)) {
            return;
        }

        try {
            location.getBlock().setType(Material.AIR);
            RevivalMode.chests.remove(location);
        } catch(NullPointerException e) {
            logger.error("Unable to alter the chest information.");
        }
    }
}
