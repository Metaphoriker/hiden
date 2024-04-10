package dev.luzifer.hiden.items.itemhandler.impl.grenades;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.DecoyGrenade;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DecoyGrenadeItemHandler extends GrenadeItemHandler {

  public DecoyGrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Item item = throwItem(player, HideAndSeekGame.DECOY_GRENADE_ITEM);

    DecoyGrenade decoyGrenade = new DecoyGrenade(item, getPlayerTracker());
    decoyGrenade.start();
  }
}
