package dev.luzifer.hiden.items.itemhandler;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.game.LinkageManager;
import dev.luzifer.hiden.game.PlayerTracker;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class BaseItemHandler implements ItemHandler {

  protected static final long THREE_SECONDS_IN_MILLIS = 1000 * 3;
  protected static final ItemStack FOUND_COMPASS_ITEM = new ItemStack(Material.COMPASS);

  static {
    ItemMeta itemMeta = FOUND_COMPASS_ITEM.getItemMeta();
    itemMeta.setDisplayName("§a§lTeleporter");
    FOUND_COMPASS_ITEM.setItemMeta(itemMeta);
  }

  private final Map<UUID, Long> itemCooldown = new HashMap<>();

  private final HideAndSeekGame hideAndSeekGame;

  @Getter(value = AccessLevel.PROTECTED)
  private final PlayerTracker playerTracker;

  @Getter(value = AccessLevel.PROTECTED)
  private final LinkageManager linkageManager;

  private final ItemHandlerConfiguration itemHandlerConfiguration;

  public BaseItemHandler(
      HideAndSeekGame hideAndSeekGame, ItemHandlerConfiguration itemHandlerConfiguration) {
    this.hideAndSeekGame = hideAndSeekGame;
    this.playerTracker = hideAndSeekGame.getPlayerTracker();
    this.linkageManager = hideAndSeekGame.getLinkageManager();
    this.itemHandlerConfiguration = itemHandlerConfiguration;
  }

  @Override
  public void handleItem(Player player, ItemStack itemStack) {
    if (itemHandlerConfiguration.isCooldown() && hasCooldown(player)) {
      return;
    }

    handleItemUsage(player, itemStack);
    reduceItem(itemStack);
  }

  protected void setFound(Player player) {
    playerTracker.setFoundState(player.getUniqueId());
    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(hideAndSeekGame.getStartPoint());
    player.getInventory().clear();
    player.getInventory().addItem(FOUND_COMPASS_ITEM);
    hidePlayerForGameParticipants(player);
  }

  protected boolean isGameRunning() {
    return hideAndSeekGame.isRunning();
  }

  private void hidePlayerForGameParticipants(Player player) {
    player.setAllowFlight(true);
    player.setFlying(true);
    Bukkit.getOnlinePlayers()
        .forEach(
            onlinePlayer -> {
              if (playerTracker.isHider(onlinePlayer.getUniqueId())
                  || playerTracker.isSeeker(onlinePlayer.getUniqueId())) {
                onlinePlayer.hidePlayer(HidenPlugin.getInstance(), player);
              }
            });
  }

  private boolean hasCooldown(Player player) {
    UUID playerId = player.getUniqueId();
    Long lastUsedTime = itemCooldown.get(playerId);
    long currentTime = System.currentTimeMillis();

    if (lastUsedTime != null && hasNotPassedCooldown(lastUsedTime, currentTime)) {
      int remainingSeconds = calculateRemainingSeconds(lastUsedTime, currentTime);
      sendRemainingSecondsInfo(player, remainingSeconds);
      return true;
    }

    itemCooldown.put(playerId, currentTime);
    return false;
  }

  private static void sendRemainingSecondsInfo(Player player, int remainingSeconds) {
    player.sendMessage(
        String.format(
            "§cDu musst noch §4%d §cSekunden warten bevor du wieder ein Item benutzen kannst!",
            remainingSeconds));
  }

  private boolean hasNotPassedCooldown(long lastUsedTime, long currentTime) {
    return currentTime - lastUsedTime < THREE_SECONDS_IN_MILLIS;
  }

  private int calculateRemainingSeconds(long lastUsedTime, long currentTime) {
    return (int) ((THREE_SECONDS_IN_MILLIS - (currentTime - lastUsedTime)) / 1000);
  }

  private void reduceItem(ItemStack itemStack) {
    if (itemHandlerConfiguration.isReduceItem()) {
      itemStack.setAmount(itemStack.getAmount() - 1);
    }
  }

  protected abstract void handleItemUsage(Player player, ItemStack itemStack);
}
