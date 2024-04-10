package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LinkageItemHandler extends BaseItemHandler {

  public LinkageItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    if (isLinked(player)) {
      if (getPlayerTracker()
          .isFound(getLinkageManager().getLinkageMap().get(player.getUniqueId()))) {
        player.sendMessage("§cDein Partner wurde gefunden. Das ist leider nicht mehr möglich.");
        return;
      }

      Player linked =
          Bukkit.getPlayer(getLinkageManager().getLinkageMap().get(player.getUniqueId()));

      if (linked == null) {
        player.sendMessage("§cDein Partner ist nicht mehr online.");
        return;
      }

      player.teleport(linked);
      player.sendMessage("§aDu hast deine Verlinkung benutzt.");

      linked.sendMessage("§aDein Partner hat die Verlinkung benutzt.");

      getLinkageManager().getLinkageMap().remove(linked.getUniqueId());
      getLinkageManager().getLinkageMap().remove(player.getUniqueId());

      Arrays.stream(linked.getInventory().getContents())
          .filter(item -> item != null && item.isSimilar(HideAndSeekGame.LINKAGE_ITEM))
          .findFirst()
          .ifPresent(item -> item.setAmount(item.getAmount() - 1));
    } else {
      player.sendMessage("§cDu bist mit keinem Spieler verlinkt.");
    }
  }

  private boolean isLinked(Player player) {
    return getLinkageManager().getLinkageMap().containsKey(player.getUniqueId())
        || getLinkageManager().getLinkageMap().containsValue(player.getUniqueId());
  }
}
