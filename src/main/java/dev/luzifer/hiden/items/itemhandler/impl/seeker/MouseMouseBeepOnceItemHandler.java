package dev.luzifer.hiden.items.itemhandler.impl.seeker;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MouseMouseBeepOnceItemHandler extends BaseItemHandler {

  public MouseMouseBeepOnceItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Player closestPlayer = findClosestPlayer(player);
    if (closestPlayer != null) {
      playSoundAtPlayerLocation(closestPlayer);
    }
  }

  private Player findClosestPlayer(Player player) {
    Player closestPlayer = null;
    double closestDistance = Double.MAX_VALUE;
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (onlinePlayer == player) {
        continue;
      }
      double distance = onlinePlayer.getLocation().distance(player.getLocation());
      if (distance < closestDistance && getPlayerTracker().isHider(onlinePlayer.getUniqueId())) {
        closestDistance = distance;
        closestPlayer = onlinePlayer;
      }
    }
    return closestPlayer;
  }

  private void playSoundAtPlayerLocation(Player player) {
    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 10, 1);
  }
}
