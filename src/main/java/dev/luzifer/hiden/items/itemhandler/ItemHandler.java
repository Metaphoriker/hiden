package dev.luzifer.hiden.items.itemhandler;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemHandler {

  void handleItem(Player player, ItemStack itemStack);
}
