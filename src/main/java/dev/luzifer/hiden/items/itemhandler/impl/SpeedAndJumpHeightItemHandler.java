package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedAndJumpHeightItemHandler extends BaseItemHandler {

  public SpeedAndJumpHeightItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1));
    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 1));
    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, -2);
  }
}
