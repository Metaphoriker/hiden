package dev.luzifer.hiden.items;

import dev.luzifer.hiden.game.PlayerTracker;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlashGrenade extends BaseGrenade {

  private static final int BLINDNESS_DURATION = 20 * 3;
  private boolean exploded;

  public FlashGrenade(Item item, Player thrower, PlayerTracker playerTracker) {
    super(item, thrower, playerTracker, 20 * 2, 20 * 3, 15);
  }

  @Override
  public void untilActivation() {
    playActivationSound();
  }

  @Override
  public void afterActivation() {
    int amplifier = calculateAmplifier();

    if (!exploded) {
      playExplosionSound();
      exploded = true; // for one time sound
    }

    applyBlindnessEffectToPlayersInReach(amplifier);
  }

  @Override
  public void onDone() {}

  private void playActivationSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 2);
  }

  private void playExplosionSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 4);
  }

  private int calculateAmplifier() {
    if (ticks <= 20) {
      return 3;
    } else if (ticks >= 40) {
      return 1;
    } else {
      return 2;
    }
  }

  private void applyBlindnessEffectToPlayersInReach(int amplifier) {
    getLocation().getWorld().getNearbyEntities(getLocation(), radius, radius, radius).stream()
        .filter(Player.class::isInstance)
        .map(Player.class::cast)
        .filter(this::isTurnedWithBackToGrenade)
        .forEach(player -> applyBlindnessEffectToPlayer(player, amplifier));
  }

  private boolean isTurnedWithBackToGrenade(Player player) {
    return player
            .getLocation()
            .getDirection()
            .dot(getLocation().toVector().subtract(player.getLocation().toVector()))
        > 0;
  }

  private void applyBlindnessEffectToPlayer(Player player, int amplifier) {
    player.getWorld().spawnParticle(Particle.FLASH, player.getEyeLocation(), 1, 0, 0, 0, 0);
    player.addPotionEffect(
        new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION, amplifier, false, false));
  }
}
