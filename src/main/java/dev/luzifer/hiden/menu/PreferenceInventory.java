package dev.luzifer.hiden.menu;

import dev.luzifer.hiden.game.HideAndSeekGame;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PreferenceInventory implements InventoryHolder {

  private final Inventory inventory;
  private final HideAndSeekGame hideAndSeekGame;

  public PreferenceInventory(HideAndSeekGame hideAndSeekGame) {
    this.inventory = Bukkit.createInventory(this, 9, "§8Präferenz");
    this.hideAndSeekGame = hideAndSeekGame;

    fill();
  }

  public void handleInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);

    if (event.getClickedInventory() != inventory) {
      return;
    }

    if (event.getSlot() == 4) {
      if (hideAndSeekGame.getPlayerTracker().hasPreference(event.getWhoClicked().getUniqueId())) {
        event
            .getWhoClicked()
            .sendMessage("§cDu hast dich bereits als freiwilliger Sucher eingetragen.");
        event.getWhoClicked().closeInventory();
        return;
      }

      hideAndSeekGame.getPlayerTracker().addSeekerPreference(event.getWhoClicked().getUniqueId());
      event.getWhoClicked().sendMessage("§aDu hast dich als freiwilliger Sucher eingetragen.");
      event.getWhoClicked().closeInventory();
    }
  }

  private void fill() {
    ItemStack seeker = new ItemStack(Material.PAPER);
    ItemMeta seekerMeta = seeker.getItemMeta();
    seekerMeta.setDisplayName("§cSucher");
    seekerMeta.setLore(List.of("§7§oKlicke hier um deine Präferenz als Sucher zu setzen."));
    seekerMeta.setCustomModelData(1001);
    seeker.setItemMeta(seekerMeta);

    inventory.setItem(4, seeker);
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
