package dev.luzifer.hiden.listener.world;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemPickupListener extends GameListener {

  public ItemPickupListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onPickupItems(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player player) {
      if (getHideAndSeekGame().getPlayerTracker().isFound(player.getUniqueId())) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onItemDrop(PlayerDropItemEvent event) {
    if (getHideAndSeekGame().isRunning()) {
      event.setCancelled(true);
    }
  }
}
