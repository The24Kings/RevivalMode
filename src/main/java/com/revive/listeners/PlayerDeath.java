package com.revive.listeners;

import com.revive.RevivalMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.slf4j.Logger;

import static com.revive.RevivalMode.getPlugin;

public class PlayerDeath  implements Listener {
    private static final RevivalMode plugin = getPlugin();
    private final Logger logger = plugin.getSLF4JLogger();

    boolean killedByPlayer(Player attacker) {
        return attacker != null;
    }

    boolean naturalDeath(Player attacker) {
        return attacker == null;
    }

    private ItemStack playerSoul(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        String skullName = "Soul of " + player.getName();

        skull.editMeta(SkullMeta.class, meta -> {
            meta.getPersistentDataContainer().set(RevivalMode.key, PersistentDataType.STRING, skullName);
            meta.displayName(Component.text(skullName).color(NamedTextColor.GOLD));
            meta.setOwningPlayer(player);
        });

        return skull;
    }

    @EventHandler
    public void onPlayerPVPDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player attacker = player.getKiller();

        if (!killedByPlayer(attacker)) {
            return;
        }

        // Sets dead player into spectator
        player.setGameMode(GameMode.SPECTATOR);

        // Creates player skull from dead player
        ItemStack skull = playerSoul(player);

        // Give attacker item unless inv. full
        if (player.getInventory().firstEmpty() == -1) {
            Location location = attacker.getLocation();
            World world = attacker.getWorld();

            world.dropItemNaturally(location, skull);
        } else {
            attacker.getInventory().addItem(skull);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player attacker = player.getKiller();

        if (!naturalDeath(attacker)) {
            return;
        }

        // Place chest at player's death location
        Block block = event.getPlayer().getLocation().getBlock();
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        Inventory inventory = chest.getInventory();

        // Sets dead player into spectator
        player.setGameMode(GameMode.SPECTATOR);

        // Adds chest Location to list of active chests
        RevivalMode.chests.add(block.getLocation());

        // Add skull to chest
        inventory.addItem(playerSoul(player));
    }
}
