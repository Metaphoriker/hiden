package dev.luzifer.hiden.items.itemhandler;

import dev.luzifer.hiden.game.HideAndSeekGame;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GrenadeItemHandler extends BaseItemHandler {

  public GrenadeItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  protected static @NotNull Item throwItem(Player player, ItemStack itemStack) {
    Item item = player.getWorld().dropItem(player.getEyeLocation(), itemStack.clone());
    item.setVelocity(player.getLocation().getDirection().multiply(1.5));
    item.setPickupDelay(Integer.MAX_VALUE);
    item.setInvulnerable(true);
    item.setGlowing(true);
    return item;
  }
}
