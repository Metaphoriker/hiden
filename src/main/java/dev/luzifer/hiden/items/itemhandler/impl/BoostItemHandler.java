package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BoostItemHandler extends BaseItemHandler {

  private final Map<UUID, Long> boostCooldown = new HashMap<>();

  public BoostItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createOppositeToDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    if (hasBoostCooldown(player)) return;

    player.setVelocity(player.getLocation().getDirection().multiply(1.75));
    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, -2);
  }

  private boolean hasBoostCooldown(Player player) {
    UUID playerId = player.getUniqueId();
    Long lastUsedTime = boostCooldown.get(playerId);
    long currentTime = System.currentTimeMillis();

    if (lastUsedTime != null && hasNotPassedBoostCooldown(lastUsedTime, currentTime)) {
      int remainingSeconds = calculateRemainingBoostSeconds(lastUsedTime, currentTime);
      player.sendMessage(
          String.format(
              "§cDu musst noch §4%d §cSekunden warten bevor du den Boost wieder benutzen kannst!",
              remainingSeconds));
      return true;
    }

    boostCooldown.put(playerId, currentTime);
    return false;
  }

  private boolean hasNotPassedBoostCooldown(long lastUsedTime, long currentTime) {
    return currentTime - lastUsedTime < ((THREE_SECONDS_IN_MILLIS * 2) - 2000);
  }

  private int calculateRemainingBoostSeconds(long lastUsedTime, long currentTime) {
    return (int) ((((THREE_SECONDS_IN_MILLIS * 2) - 2000) - (currentTime - lastUsedTime)) / 1000);
  }
}
