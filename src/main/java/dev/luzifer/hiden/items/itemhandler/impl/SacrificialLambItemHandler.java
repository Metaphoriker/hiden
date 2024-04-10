package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SacrificialLambItemHandler extends BaseItemHandler {

  public SacrificialLambItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    Player randomHider = getRandomHider(player);

    if (randomHider == null) {
      player.sendMessage("§cEs gibt keine anderen Hider.");
      return;
    }

    swapPlayerLocationsAndSendMessages(player, randomHider);
  }

  private Player getRandomHider(Player player) {
    List<UUID> hiders = new ArrayList<>();
    getPlayerTracker().forEachHider(hiders::add);
    hiders.remove(player.getUniqueId());

    if (hiders.isEmpty()) {
      return null;
    }

    UUID randomHiderId = hiders.get((int) (Math.random() * hiders.size()));
    return Bukkit.getPlayer(randomHiderId);
  }

  private void swapPlayerLocationsAndSendMessages(Player player, Player randomHider) {
    Location playerLocation = player.getLocation();
    Location randomHiderLocation = randomHider.getLocation();

    player.teleport(randomHiderLocation);
    randomHider.teleport(playerLocation);

    boolean isSeekerNearby = isSeekerNearby(randomHiderLocation);

    String message =
        isSeekerNearby
            ? "§aDu wurdest geopfert!"
            : "§aDu wurdest mit einem anderen Hider getauscht.";

    player.sendMessage(message);

    if (isSeekerNearby) {
      randomHider.sendTitle("", message, 0, 40, 0);
    } else {
      randomHider.sendMessage(message);
    }

    sendEndermanEffectParticleWithRandomOffsets(player);
    sendEndermanEffectParticleWithRandomOffsets(randomHider);
  }

  private void sendEndermanEffectParticleWithRandomOffsets(Player player) {
    for (int i = 0; i < 10; i++) {
      double randomX = Math.random() * 0.5 - 0.25;
      double randomY = Math.random() * 0.5 - 0.25;
      double randomZ = Math.random() * 0.5 - 0.25;

      player
          .getWorld()
          .spawnParticle(
              Particle.PORTAL, player.getLocation().add(randomX, randomY, randomZ), 1, 0, 0, 0, 0);
    }
  }

  private boolean isSeekerNearby(Location location) {
    double thresholdDistance = 20.0;
    return Bukkit.getOnlinePlayers().stream()
        .filter(p -> getPlayerTracker().isSeeker(p.getUniqueId()))
        .anyMatch(seeker -> seeker.getLocation().distance(location) <= thresholdDistance);
  }
}
