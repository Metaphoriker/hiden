package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// TODO: for this item it needs a clicktype
public class NitroGauntletItemHandler extends BaseItemHandler {

  public NitroGauntletItemHandler(
      HideAndSeekGame hideAndSeekGame, ItemHandlerConfiguration itemHandlerConfiguration) {
    super(hideAndSeekGame, itemHandlerConfiguration);
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
  }
}
