package dev.luzifer.hiden.items.itemhandler.impl;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.items.itemhandler.BaseItemHandler;
import dev.luzifer.hiden.items.itemhandler.ItemHandlerConfiguration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class PepperSprayItemHandler extends BaseItemHandler {

  public PepperSprayItemHandler(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame, ItemHandlerConfiguration.createDefault());
  }

  @Override
  protected void handleItemUsage(Player player, ItemStack itemStack) {
    sprayPepperSpray(player);
  }

  private void sprayPepperSpray(Player player) {
    Vector direction = player.getLocation().getDirection();
    playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, -3);
    spawnParticles(
        player, direction, 20, Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 1));
    applyEffectsToHitEntity(
        player,
        direction,
        20,
        0.2,
        PotionEffectType.BLINDNESS,
        PotionEffectType.SLOW,
        PotionEffectType.CONFUSION);
  }

  private void playSound(Location location, Sound sound, float volume, float pitch) {
    location.getWorld().playSound(location, sound, volume, pitch);
  }

  private void spawnParticles(
      Player player,
      Vector direction,
      int range,
      Particle particle,
      Particle.DustOptions dustOptions) {
    for (int i = 0; i < range; i++) {
      Location location = player.getEyeLocation().add(direction.clone().multiply(i));
      player.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0, dustOptions);
    }
  }

  private void applyEffectsToHitEntity(
      Player player, Vector direction, double range, double raySize, PotionEffectType... effects) {
    LivingEntity hitEntity = getHitEntity(player, direction, range, raySize);
    if (hitEntity != null) {
      applyEffects(hitEntity, effects);
    }
  }

  private LivingEntity getHitEntity(Player player, Vector direction, double range, double raySize) {
    RayTraceResult rayTraceResult =
        player
            .getWorld()
            .rayTraceEntities(
                player.getEyeLocation(),
                direction,
                range,
                raySize,
                entity -> entity != player && entity instanceof LivingEntity);
    return rayTraceResult != null ? (LivingEntity) rayTraceResult.getHitEntity() : null;
  }

  private void applyEffects(LivingEntity hitEntity, PotionEffectType... effects) {
    for (PotionEffectType effect : effects) {
      hitEntity.addPotionEffect(new PotionEffect(effect, 20 * 8, 2));
    }
  }
}
