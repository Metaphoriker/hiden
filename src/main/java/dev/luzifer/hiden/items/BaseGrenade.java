package dev.luzifer.hiden.items;

import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.game.PlayerTracker;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseGrenade implements Grenade {

  private final Item item;
  private final int ticksUntilActivation;
  private final int ticksOfActivation;
  protected final double radius;
  protected final PlayerTracker playerTracker;

  protected int ticks;
  private boolean done;

  public BaseGrenade(
      Item representer,
      PlayerTracker playerTracker,
      int ticksUntilActivation,
      int ticksOfActivation,
      double radius) {
    this.item = representer;
    this.playerTracker = playerTracker;
    this.ticksUntilActivation = ticksUntilActivation;
    this.ticksOfActivation = ticksOfActivation;
    this.radius = radius;
  }

  public void run() {
    new BukkitRunnable() {
      @Override
      public void run() {
        tick();
        if (done) cancel();
      }
    }.runTaskTimer(HidenPlugin.getInstance(), 0, 1);
  }

  @Override
  public void tick() {
    if (ticks < ticksUntilActivation) {
      if (ticks % 10 == 0) {
        showRadiusInACircleOutline();
      }
      untilActivation();
    } else if (ticks < ticksOfActivation) {
      afterActivation();
    } else {
      done = true;
      item.remove();
      onDone();
    }
    ticks++;
  }

  public boolean isDone() {
    return done;
  }

  public Location getLocation() {
    return item.getLocation();
  }

  protected void showRadiusInACircleOutline() {
    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
    int numParticles = 50;

    for (int i = 0; i < numParticles; i++) {
      double angle = 2 * Math.PI * i / numParticles;

      double x = radius * Math.cos(angle);
      double z = radius * Math.sin(angle);

      getLocation()
          .getWorld()
          .spawnParticle(
              Particle.REDSTONE, getLocation().clone().add(x, 0, z), 1, 0, 0, 0, 0, dustOptions);
    }
  }

  public abstract void untilActivation();

  public abstract void afterActivation();

  public abstract void onDone();
}
