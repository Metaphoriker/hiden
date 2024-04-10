package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityItemHandler extends BaseItemHandler {

  public InvisibilityItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    player.addPotionEffect(
        new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 1, false, false));
    player.sendMessage("§aDu bist nun unsichtbar.");
    player
        .getLocation()
        .getWorld()
        .playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 3);
  }
}
