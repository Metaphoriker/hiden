package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DisappearItemHandler extends BaseItemHandler {

  public DisappearItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    if (!player.isGlowing()) {
      player.sendMessage("§cDu bist aktuell nicht auf dem Radar.");
      return;
    }

    player.setGlowing(false);
    player.sendMessage("§aDu bist nun nicht mehr auf dem Radar.");
    player.playSound(player.getLocation(), Sound.ENTITY_GLOW_SQUID_DEATH, 1, 2);

    showPlayerTask(player);
  }

  private void showPlayerTask(Player player) {
    Bukkit.getScheduler()
        .runTaskLater(
            HidenPlugin.getInstance(),
            () -> {
              if (isGameRunning()) {
                player.setGlowing(true);
                player.sendMessage("§aDu bist nun wieder auf dem Radar.");
              }
            },
            20 * 20);
  }
}
