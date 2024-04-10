package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener extends GameListener {

  public PlayerDamageListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player player) {
      handleEntityDamage(event, player);
    }
  }

  private void handleEntityDamage(EntityDamageEvent event, Player player) {
    switch (event.getCause()) {
      case DROWNING:
      case LAVA:
      case FIRE:
      case FIRE_TICK:
      case SUFFOCATION:
        event.setCancelled(true);
        break;
      case VOID:
      case WORLD_BORDER:
        handlePlayerExit(player);
        break;
      default:
        event.setDamage(0);
        break;
    }
  }

  private void handlePlayerExit(Player player) {
    Bukkit.getScheduler()
        .runTask(
            HidenPlugin.getInstance(),
            () -> {
              if (getHideAndSeekGame().getPlayerTracker().isHider(player.getUniqueId())) {
                setFound(player);
                broadcastPlayerExitMessage(player);
              } else {
                player.teleport(getHideAndSeekGame().getStartPoint());
              }
            });
  }
}
