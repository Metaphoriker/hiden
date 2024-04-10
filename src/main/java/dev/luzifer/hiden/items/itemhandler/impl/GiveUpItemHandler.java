package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveUpItemHandler extends BaseItemHandler {

  public GiveUpItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createOppositeToDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    if (getPlayerTracker().isHider(player.getUniqueId())) {
      setFound(player);
      broadcastPlayerExitMessage(player);
    }
  }

  private void broadcastPlayerExitMessage(Player player) {
    Bukkit.broadcastMessage(
        "§c"
            + player.getName()
            + " ist ausgeschieden! "
            + "§7[§c"
            + getPlayerTracker().getFoundCount()
            + "§7/§b"
            + getPlayerTracker().getHiderCount()
            + "§7]");
  }
}
