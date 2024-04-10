package dev.luzifer.hiden.menu;

import dev.luzifer.hiden.game.PlayerTracker;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class ScrollableOnlinePlayerHeadInventory implements InventoryHolder {

  private final Inventory inventory;
  private final PlayerTracker playerTracker;

  private int row;

  public ScrollableOnlinePlayerHeadInventory(PlayerTracker playerTracker) {
    this.inventory = Bukkit.createInventory(this, 54, "§7Klicke zum Teleportieren");
    this.playerTracker = playerTracker;
    fill();
  }

  private void fill() {
    inventory.clear();
    setControlPanel();
    fillWithPlayerHeads();
  }

  private ItemStack createPlayerHead(Player player) {
    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
    skullMeta.setOwningPlayer(player);
    String prefix = playerTracker.isSeeker(player.getUniqueId()) ? "§c" : "§9";
    skullMeta.setDisplayName(prefix + player.getName());
    head.setItemMeta(skullMeta);
    return head;
  }

  private void fillWithPlayerHeads() {
    var onlinePlayers = List.copyOf(Bukkit.getOnlinePlayers());
    var listWithoutFound =
        onlinePlayers.stream()
            .filter(player -> !playerTracker.isFound(player.getUniqueId()))
            .toList();

    int index = 8 * row;
    for (int i = 0; i != 46; i++) {
      if (index >= listWithoutFound.size()) {
        break;
      }

      inventory.addItem(createPlayerHead(listWithoutFound.get(index)));
      index++;
    }
  }

  private void setControlPanel() {
    ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    ItemMeta fillerMeta = filler.getItemMeta();
    fillerMeta.setDisplayName("§9");
    filler.setItemMeta(fillerMeta);

    for (int i = 1; i != 6; i++) {
      int slot = 8 * i + (i - 1);
      inventory.setItem(slot, filler);
    }

    ItemStack upArrow = new ItemStack(Material.ARROW);
    ItemMeta upArrowMeta = upArrow.getItemMeta();
    upArrowMeta.setDisplayName("§aNach oben scrollen");
    upArrow.setItemMeta(upArrowMeta);
    inventory.setItem(26, upArrow);

    ItemStack downArrow = new ItemStack(Material.ARROW);
    ItemMeta downArrowMeta = downArrow.getItemMeta();
    downArrowMeta.setDisplayName("§aNach unten scrollen");
    downArrow.setItemMeta(downArrowMeta);
    inventory.setItem(35, downArrow);
  }

  public void handleClick(InventoryClickEvent event) {
    event.setCancelled(true);

    if (event.getSlot() == 26) {
      scrollUp();
    } else if (event.getSlot() == 35) {
      scrollDown();
    } else {
      teleportToPlayer((Player) event.getWhoClicked(), event.getSlot());
    }
  }

  private void scrollUp() {
    if (row == 0) {
      return;
    }

    row--;
    fill();
  }

  private void scrollDown() {
    var onlinePlayers = List.copyOf(Bukkit.getOnlinePlayers());
    int maxRows = (onlinePlayers.size() - 1) / 8;
    if (row == maxRows) {
      return;
    }

    row++;
    fill();
  }

  private void teleportToPlayer(Player player, int slot) {
    if (slot < 0 || slot >= inventory.getSize()) {
      return;
    }

    ItemStack item = inventory.getItem(slot);
    if (item == null || item.getType() != Material.PLAYER_HEAD) {
      return;
    }

    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
    if (skullMeta == null || skullMeta.getOwningPlayer() == null) {
      return;
    }

    Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getUniqueId());
    if (target == null) {
      return;
    }

    player.teleport(target);
    player.closeInventory();
  }

  @NotNull
  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
