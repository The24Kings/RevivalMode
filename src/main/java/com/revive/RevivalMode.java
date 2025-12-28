package com.revive;

import com.revive.listeners.ChestClose;
import com.revive.listeners.HeadPlace;
import com.revive.listeners.PlayerDeath;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The24Kings.
 *
 * @author Copyright (c) The24Kings. All Rights Reserved.
 */
public class RevivalMode extends JavaPlugin {
    private final Logger logger = this.getSLF4JLogger();
    public static RevivalMode plugin;
    public static final List<Location> chests = new ArrayList<>();
    public static final NamespacedKey key = new NamespacedKey(plugin, "revive");

    @Override
    public void onEnable() {
        plugin = this;

        PaperLib.suggestPaper(plugin);
        registerCommandsAndEvents();

        logger.info("[RevivalMode] Has started Successfully");

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("[RevivalMode] Disabled");
    }

    private void registerCommandsAndEvents() {
        getServer().getPluginManager().registerEvents(new ChestClose(), plugin);
        getServer().getPluginManager().registerEvents(new HeadPlace(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), plugin);
    }
}
