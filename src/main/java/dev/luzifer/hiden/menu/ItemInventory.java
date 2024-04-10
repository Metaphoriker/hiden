package dev.luzifer.hiden.menu;

import dev.luzifer.hiden.game.HideAndSeekGame;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ItemInventory implements InventoryHolder {

  private final Inventory inventory;

  public ItemInventory() {
    inventory = Bukkit.createInventory(this, 27, "§6§lSpiel-Items");
    fill();
  }

  public void handleClick(InventoryClickEvent event) {
    event.setCancelled(true);

    if (event.getCurrentItem() != null && event.getWhoClicked().isOp()) {
      event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
    }
  }

  private void fill() {
    Arrays.stream(HideAndSeekGame.ALL_ITEMS).forEach(inventory::addItem);
  }

  @NotNull
  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
