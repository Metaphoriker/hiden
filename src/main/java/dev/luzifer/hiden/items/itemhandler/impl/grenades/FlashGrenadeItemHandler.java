package dev.luzifer.hiden.items.itemhandler.impl.grenades;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.FlashGrenade;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlashGrenadeItemHandler extends GrenadeItemHandler {

  public FlashGrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Item item = throwItem(player, HideAndSeekGame.FLASH_GRENADE_ITEM);

    FlashGrenade flashGrenade = new FlashGrenade(item, getPlayerTracker());
    flashGrenade.run();
  }
}
