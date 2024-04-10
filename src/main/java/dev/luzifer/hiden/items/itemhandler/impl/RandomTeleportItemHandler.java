package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RandomTeleportItemHandler extends BaseItemHandler {

  private static final double RADIUS = 30;

  public RandomTeleportItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    randomTeleportPlayer(player);
  }

  private void randomTeleportPlayer(Player player) {
    double x = player.getLocation().getX() + (Math.random() * RADIUS * 2) - RADIUS;
    double z = player.getLocation().getZ() + (Math.random() * RADIUS * 2) - RADIUS;
    double y = player.getWorld().getHighestBlockYAt((int) x, (int) z);
    player.teleport(new Location(player.getWorld(), x, y + 1, z));
  }
}
