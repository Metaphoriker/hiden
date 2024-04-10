package dev.luzifer.hiden.listener;

import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.game.HideAndSeekGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GameListener implements Listener {
  protected static final ItemStack FOUND_COMPASS_ITEM = new ItemStack(Material.COMPASS);

  static {
    ItemMeta itemMeta = FOUND_COMPASS_ITEM.getItemMeta();
    itemMeta.setDisplayName("§a§lTeleporter");
    FOUND_COMPASS_ITEM.setItemMeta(itemMeta);
  }

  private final HideAndSeekGame hideAndSeekGame;

  public GameListener(HideAndSeekGame hideAndSeekGame) {
    this.hideAndSeekGame = hideAndSeekGame;
  }

  protected void setFound(Player player) {
    hideAndSeekGame.getPlayerTracker().setFoundState(player.getUniqueId());
    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(hideAndSeekGame.getStartPoint());
    player.getInventory().clear();
    player.getInventory().addItem(FOUND_COMPASS_ITEM);
    hidePlayerForGameParticipants(player);
  }

  private void hidePlayerForGameParticipants(Player player) {
    player.setAllowFlight(true);
    player.setFlying(true);
    Bukkit.getOnlinePlayers()
        .forEach(
            onlinePlayer -> {
              if (hideAndSeekGame.getPlayerTracker().isHider(onlinePlayer.getUniqueId())
                  || hideAndSeekGame.getPlayerTracker().isSeeker(onlinePlayer.getUniqueId())) {
                onlinePlayer.hidePlayer(HidenPlugin.getInstance(), player);
              }
            });
  }

  protected void broadcastPlayerExitMessage(Player player) {
    Bukkit.broadcastMessage(
        "§c"
            + player.getName()
            + " ist ausgeschieden! "
            + "§7[§c"
            + getHideAndSeekGame().getPlayerTracker().getFoundCount()
            + "§7/§b"
            + getHideAndSeekGame().getPlayerTracker().getHiderCount()
            + "§7]");
  }

  protected HideAndSeekGame getHideAndSeekGame() {
    return hideAndSeekGame;
  }
}
