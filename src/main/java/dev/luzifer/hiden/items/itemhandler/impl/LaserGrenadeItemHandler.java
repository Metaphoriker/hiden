package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.LaserGrenade;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LaserGrenadeItemHandler extends BaseItemHandler {

  public LaserGrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Item item = throwItem(player);

    LaserGrenade laserGrenade = new LaserGrenade(item, getPlayerTracker());
    laserGrenade.run();
  }

  private static @NotNull Item throwItem(Player player) {
    Item item =
        player
            .getWorld()
            .dropItem(player.getEyeLocation(), HideAndSeekGame.LASER_GRENADE_ITEM.clone());
    item.setVelocity(player.getLocation().getDirection().multiply(1.5));
    item.setPickupDelay(Integer.MAX_VALUE);
    item.setInvulnerable(true);
    item.setGlowing(true);
    return item;
  }
}
