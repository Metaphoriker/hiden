package dev.luzifer.hiden.listener.world;

import dev.luzifer.hiden.menu.ItemInventory;
import dev.luzifer.hiden.menu.PreferenceInventory;
import dev.luzifer.hiden.menu.ScrollableOnlinePlayerHeadInventory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;

public class ConvinienceListener implements Listener {

  @EventHandler
  public void onEntityDamageByEntity(HangingBreakByEntityEvent event) {
    if (event.getEntity() instanceof Painting) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void FrameEntity(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof ItemFrame) {
      if (e.getDamager() instanceof Player) {
        e.setCancelled(true);
      }
      if (e.getDamager() instanceof Projectile) {
        if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
          e.getDamager().remove();
          e.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void FrameRotate(PlayerInteractEntityEvent e) {
    if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof ArmorStand) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteractWithArmorStand(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked() instanceof ArmorStand) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getClickedInventory() != null) {
      InventoryHolder holder = event.getClickedInventory().getHolder();
      if (holder instanceof ItemInventory itemInventory) {
        itemInventory.handleClick(event);
      }

      if (holder instanceof PreferenceInventory preferenceInventory) {
        preferenceInventory.handleInventoryClick(event);
      }

      if (holder
          instanceof ScrollableOnlinePlayerHeadInventory scrollableOnlinePlayerHeadInventory) {
        scrollableOnlinePlayerHeadInventory.handleClick(event);
      }
    }
  }
}
