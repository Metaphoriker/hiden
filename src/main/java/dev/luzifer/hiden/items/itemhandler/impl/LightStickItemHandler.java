package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LightStickItemHandler extends BaseItemHandler {

  public LightStickItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 10, 1));
    player.sendTitle("", "§eDu hast dich offenbart", 0, 40, 0);
    player
        .getLocation()
        .getWorld()
        .playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 3);
  }
}
