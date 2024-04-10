package dev.luzifer.hiden.items;

import dev.luzifer.hiden.game.PlayerTracker;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;

public class SmokeGrenade extends BaseGrenade {

  private static final double PARTICLE_OFFSET_RANGE = 0.5;
  private static final double PARTICLE_DENSITY = 0.7;

  public SmokeGrenade(Item item, PlayerTracker playerTracker) {
    super(item, playerTracker, 20 * 2, 20 * 10, 7);
  }

  @Override
  public void untilActivation() {
    playActivationSound();
  }

  @Override
  public void afterActivation() {
    if (ticks % 10 == 0) {
      playImitationSound();
      sendSmokeParticlesToEveryone();
    }
  }

  @Override
  public void onDone() {}

  private void playActivationSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 2);
  }

  private void playImitationSound() {
    getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_PARROT_IMITATE_CREEPER, 1, -2);
  }

  private void sendSmokeParticlesToEveryone() {
    double currentRadius = Math.min(radius, ticks / 10.0);

    for (double x = -currentRadius; x <= currentRadius; x += PARTICLE_DENSITY) {
      for (double y = -currentRadius; y <= currentRadius; y += PARTICLE_DENSITY) {
        for (double z = -currentRadius; z <= currentRadius; z += PARTICLE_DENSITY) {
          if (isWithinCurrentRadius(x, y, z, currentRadius)) {
            spawnSmokeParticleAtOffset(x, y, z);
          }
        }
      }
    }
  }

  private boolean isWithinCurrentRadius(double x, double y, double z, double currentRadius) {
    return Math.sqrt(x * x + y * y + z * z) <= currentRadius;
  }

  private void spawnSmokeParticleAtOffset(double offsetX, double offsetY, double offsetZ) {
    double randomXOffset =
        ThreadLocalRandom.current().nextDouble(-PARTICLE_OFFSET_RANGE, PARTICLE_OFFSET_RANGE);
    double randomZOffset =
        ThreadLocalRandom.current().nextDouble(-PARTICLE_OFFSET_RANGE, PARTICLE_OFFSET_RANGE);

    getLocation()
        .getWorld()
        .spawnParticle(
            Particle.SMOKE_LARGE,
            getLocation().clone().add(offsetX + randomXOffset, offsetY, offsetZ + randomZOffset),
            1,
            0,
            0,
            0,
            0);
  }
}
