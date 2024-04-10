package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.GameListener;
import dev.luzifer.hiden.menu.ScrollableOnlinePlayerHeadInventory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerUseItemListener extends GameListener {

  public PlayerUseItemListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    if (event.getAction() == Action.RIGHT_CLICK_AIR
        || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;

      ItemStack item = event.getItem();

      if (getHideAndSeekGame().isRunning()) {
        if (getHideAndSeekGame().getPlayerTracker().isFound(event.getPlayer().getUniqueId())) {
          if (item.isSimilar(FOUND_COMPASS_ITEM)) {
            event.setCancelled(true);
            ScrollableOnlinePlayerHeadInventory inventory =
                new ScrollableOnlinePlayerHeadInventory(getHideAndSeekGame().getPlayerTracker());
            event.getPlayer().openInventory(inventory.getInventory());
          }
          return;
        }

        getHideAndSeekGame().handleItem(event.getPlayer(), item);
        event.setCancelled(true);
      }
    }
  }
}
