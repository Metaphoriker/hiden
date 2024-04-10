package dev.luzifer.hiden.items;

import dev.luzifer.hiden.game.PlayerTracker;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Sound;
import org.bukkit.entity.Item;

public class DecoyGrenade extends BaseGrenade {

  public DecoyGrenade(Item representer, PlayerTracker playerTracker) {
    super(representer, playerTracker, 20 * 5, 20, 30);
  }

  @Override
  public void untilActivation() {
    simulatePlayerStep();
  }

  private void simulatePlayerStep() {
    if (ThreadLocalRandom.current().nextInt(0, 101) <= 10) {
      getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_STONE_STEP, 1, 1);
    }
  }

  @Override
  public void afterActivation() {}

  @Override
  public void onDone() {}
}
