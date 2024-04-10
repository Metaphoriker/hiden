package dev.luzifer.hiden.items.itemhandler.impl.grenades;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.SmokeGrenade;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SmokeGrenadeItemHandler extends GrenadeItemHandler {

  public SmokeGrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Item item = throwItem(player, HideAndSeekGame.SMOKE_GRENADE_ITEM);

    SmokeGrenade smokeGrenade = new SmokeGrenade(item, getPlayerTracker());
    smokeGrenade.start();
  }
}
