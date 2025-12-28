package com.revive.listeners;

import com.revive.RevivalMode;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class HeadPlace  implements Listener {
    private static final RevivalMode plugin = RevivalMode.getPlugin();
    private final Logger logger = plugin.getSLF4JLogger();

    @EventHandler
    public void onHeadPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.PLAYER_HEAD) {
            return;
        }

        PersistentDataContainerView container = event.getItemInHand().getPersistentDataContainer();

        if (!container.has(RevivalMode.key, PersistentDataType.STRING)) {
            return;
        }

        ItemStack head = event.getItemInHand();
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        Block block = event.getBlockPlaced();
        World world = block.getWorld();
        Location location = block.getLocation();

        final Collection<PotionEffect> enchantedGoldenApple = List.of(
                new PotionEffect(PotionEffectType.ABSORPTION, 20 * 120 /*2 min*/, 4, true),
                new PotionEffect(PotionEffectType.RESISTANCE, 20 * 300 /*5 min*/, 1, true),
                new PotionEffect(PotionEffectType.REGENERATION, 20 * 20 /*20 sec*/, 2, true),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 300 /*5 min*/, 1, true)
        );

        try {
            // Respawn Player
            Player player = meta.getOwningPlayer().getPlayer();
            player.teleport(location.add(0.5, 0, 0.5));

            //Effects
            world.strikeLightning(location);
            player.addPotionEffects(enchantedGoldenApple);

            for (Player players : getServer().getOnlinePlayers()) {
                players.playSound(location, Sound.ITEM_TRIDENT_THUNDER, 10, 0.3f);
            }

            player.setGameMode(GameMode.SURVIVAL);

            // Remove Head
            event.getBlock().setType(Material.AIR);

            // Remove head from Inventory
            Player respawner = event.getPlayer();
            respawner.getInventory().setItem(event.getHand(), new ItemStack(Material.AIR, 1));

        } catch (NullPointerException e) {
            event.setCancelled(true);

            logger.error("Cannot find player!");
        }
    }
}
