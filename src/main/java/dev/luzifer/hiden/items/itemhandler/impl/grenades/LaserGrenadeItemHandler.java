package dev.luzifer.hiden.items.itemhandler.impl.grenades;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.LaserGrenade;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LaserGrenadeItemHandler extends GrenadeItemHandler {

  public LaserGrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Item item = throwItem(player, HideAndSeekGame.LASER_GRENADE_ITEM);

    LaserGrenade laserGrenade = new LaserGrenade(item, getPlayerTracker());
    laserGrenade.start();
  }
}
