package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener extends GameListener {

  public PlayerJoinLeaveListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    handlePlayerJoin(event);
  }

  private void handlePlayerJoin(PlayerJoinEvent event) {
    Bukkit.getScheduler()
        .runTask(
            HidenPlugin.getInstance(),
            () -> {
              Player player = event.getPlayer();
              if (getHideAndSeekGame().isRunning()) {
                setFound(player);
              } else {
                preparePlayerForGame(player);
              }
              player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
            });
  }

  private void preparePlayerForGame(Player player) {
    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(getHideAndSeekGame().getLobbyPoint());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    handlePlayerQuit(event);
  }

  private void handlePlayerQuit(PlayerQuitEvent event) {
    if (getHideAndSeekGame().isRunning()) {
      if (getHideAndSeekGame().getPlayerTracker().isHider(event.getPlayer().getUniqueId())) {
        handleHiderQuit(event);
      } else if (getHideAndSeekGame()
          .getPlayerTracker()
          .isSeeker(event.getPlayer().getUniqueId())) {
        handleSeekerQuit(event);
      }
    }
  }

  private void handleHiderQuit(PlayerQuitEvent event) {
    getHideAndSeekGame().getPlayerTracker().removeHider(event.getPlayer().getUniqueId());
    broadcastPlayerExitMessage(event.getPlayer());
  }

  private void handleSeekerQuit(PlayerQuitEvent event) {
    getHideAndSeekGame().getPlayerTracker().removeSeeker(event.getPlayer().getUniqueId());
  }
}
