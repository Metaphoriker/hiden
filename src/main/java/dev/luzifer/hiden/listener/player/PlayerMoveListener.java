package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener extends GameListener {

  public PlayerMoveListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (getHideAndSeekGame().isHideTime()
        && getHideAndSeekGame().getPlayerTracker().isSeeker(event.getPlayer().getUniqueId())) {
      if (hasPlayerMovedToNewBlock(event)) {
        event.setCancelled(true);
      }
    }
  }

  private boolean hasPlayerMovedToNewBlock(PlayerMoveEvent event) {
    return event.getFrom().getBlockX() != event.getTo().getBlockX()
        || event.getFrom().getBlockY() != event.getTo().getBlockY()
        || event.getFrom().getBlockZ() != event.getTo().getBlockZ();
  }
}
