package dev.luzifer.hiden.items;

import dev.luzifer.hiden.game.PlayerTracker;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LaserGrenade extends BaseGrenade {

  private static final int GLOW_DURATION = 20 * 5;
  private static final int GLOW_AMPLIFIER = 1;
  private static final int BEAM_LENGTH = 5;
  private static final int NUM_BEAMS = 4;
  private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.RED, 1);

  public LaserGrenade(Item item, PlayerTracker playerTracker) {
    super(item, playerTracker, 20 * 2, 20 * 2, 20);
  }

  @Override
  public void untilActivation() {
    playActivationSound();
  }

  @Override
  public void afterActivation() {}

  @Override
  public void onDone() {
    glowEveryoneInReach();
    playExplosionSound();
  }

  private void playActivationSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 2);
  }

  private void playExplosionSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 4);
  }

  private void glowEveryoneInReach() {
    sendVisualEffectCircledAroundTheGrenade();
    sendLaserBeamsComingFromTheCenter();
    applyGlowEffectToPlayersInReach();
  }

  private void applyGlowEffectToPlayersInReach() {
    getLocation().getWorld().getNearbyEntities(getLocation(), radius, radius, radius).stream()
        .filter(Player.class::isInstance)
        .map(Player.class::cast)
        .filter(player -> !playerTracker.isSeeker(player.getUniqueId()))
        .forEach(this::applyGlowEffectToPlayer);
  }

  private void applyGlowEffectToPlayer(Player player) {
    player.addPotionEffect(
        new PotionEffect(PotionEffectType.GLOWING, GLOW_DURATION, GLOW_AMPLIFIER, false, false));
    player.sendTitle("", "§cDu wurdest offenbart!", 0, 40, 0);
  }

  private void sendVisualEffectCircledAroundTheGrenade() {
    double radius = 1;

    for (double x = -radius; x <= radius; x += 0.25) {
      for (double y = -radius; y <= radius; y += 0.25) {
        for (double z = -radius; z <= radius; z += 0.25) {
          if (Math.sqrt(x * x + y * y + z * z) <= radius) {
            spawnRedstoneParticleAtOffset(x, y, z);
          }
        }
      }
    }
  }

  private void spawnRedstoneParticleAtOffset(double offsetX, double offsetY, double offsetZ) {
    getLocation()
        .getWorld()
        .spawnParticle(
            Particle.REDSTONE,
            getLocation().clone().add(offsetX, offsetY, offsetZ),
            1,
            0,
            0,
            0,
            0,
            DUST_OPTIONS);
  }

  private void sendLaserBeamsComingFromTheCenter() {
    for (int i = 0; i < NUM_BEAMS; i++) {
      double angle = 2 * Math.PI * i / NUM_BEAMS;

      for (int j = 0; j < BEAM_LENGTH; j++) {
        double x = j * Math.cos(angle);
        double z = j * Math.sin(angle);

        spawnRedstoneParticleAtOffset(x, 0.5, z);
      }
    }
  }
}
