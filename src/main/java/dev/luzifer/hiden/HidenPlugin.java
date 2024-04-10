package dev.luzifer.hiden;

import dev.luzifer.hiden.commands.HideCommand;
import dev.luzifer.hiden.commands.LobbyCommand;
import dev.luzifer.hiden.commands.SpawnCommand;
import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.player.PlayerChatListener;
import dev.luzifer.hiden.listener.player.PlayerDamageListener;
import dev.luzifer.hiden.listener.player.PlayerHitListener;
import dev.luzifer.hiden.listener.player.PlayerJoinLeaveListener;
import dev.luzifer.hiden.listener.player.PlayerMoveListener;
import dev.luzifer.hiden.listener.player.PlayerUseItemListener;
import dev.luzifer.hiden.listener.world.BlockListener;
import dev.luzifer.hiden.listener.world.ConvinienceListener;
import dev.luzifer.hiden.listener.world.ItemPickupListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class HidenPlugin extends JavaPlugin {

  @Getter private static HidenPlugin instance;

  private final HideAndSeekGame hideAndSeekGame = new HideAndSeekGame();

  @Override
  public void onEnable() {
    instance = this;
    loadConfig();

    registerListener();
    registerCommands();
  }

  private void registerListener() {
    Bukkit.getPluginManager().registerEvents(new ConvinienceListener(), this);
    Bukkit.getPluginManager().registerEvents(new PlayerChatListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new BlockListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new ItemPickupListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new PlayerHitListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(hideAndSeekGame), this);
    Bukkit.getPluginManager().registerEvents(new PlayerUseItemListener(hideAndSeekGame), this);
  }

  private void registerCommands() {
    Bukkit.getPluginCommand("hide").setExecutor(new HideCommand(this, hideAndSeekGame));
    Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand(hideAndSeekGame));
    Bukkit.getPluginCommand("lobby").setExecutor(new LobbyCommand(hideAndSeekGame));
  }

  @Override
  public void onDisable() {}

  private void loadConfig() {
    saveDefaultConfig();
    Bukkit.getScheduler()
        .runTask(
            this,
            () -> {
              hideAndSeekGame.setStartPoint(loadSpawn());
              hideAndSeekGame.setLobbyPoint(loadLobby());
            });
  }

  public void saveSpawn() {
    getConfig().set("spawn", hideAndSeekGame.getStartPoint());
    saveConfig();
  }

  private Location loadSpawn() {
    return getConfig().getLocation("spawn");
  }

  public void saveLobby() {
    getConfig().set("lobby", hideAndSeekGame.getLobbyPoint());
    saveConfig();
  }

  private Location loadLobby() {
    return getConfig().getLocation("lobby");
  }
}
