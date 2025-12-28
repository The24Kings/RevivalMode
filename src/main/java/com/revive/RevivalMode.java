package com.revive;

import com.revive.listeners.ChestClose;
import com.revive.listeners.HeadPlace;
import com.revive.listeners.PlayerDeath;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The24Kings.
 *
 * @author Copyright (c) The24Kings. All Rights Reserved.
 */
public class RevivalMode extends JavaPlugin {
  public static RevivalMode plugin;
  public static List<Location> chests = new ArrayList<>();
  public static NamespacedKey key = new NamespacedKey(plugin, "revive");

  private final Logger logger = this.getSLF4JLogger();

  public static RevivalMode getPlugin() {
    return plugin;
  }

  public Logger getPluginLogger() {
    return logger;
  }

  public static List<Location> getActiveChests() {
    return chests;
  }

  @Override
  public void onEnable() {
    plugin = this;

    PaperLib.suggestPaper(plugin);
    registerCommandsAndEvents();

    logger.info("[ReviveSMP] Has started Successfully");

    saveDefaultConfig();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    logger.info("[ReviveSMP] Disabled");
  }

  private void registerCommandsAndEvents() {
    getServer().getPluginManager().registerEvents(new ChestClose(), plugin);
    getServer().getPluginManager().registerEvents(new HeadPlace(), plugin);
    getServer().getPluginManager().registerEvents(new PlayerDeath(), plugin);
  }
}
