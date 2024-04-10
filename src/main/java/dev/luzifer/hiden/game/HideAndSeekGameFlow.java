package dev.luzifer.hiden.game;

import java.util.Random;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HideAndSeekGameFlow extends BukkitRunnable {

  private static final int HIDE_TIME_IN_MINUTES = 2;
  private static final int TIME_IN_MINUTES = 45 + HIDE_TIME_IN_MINUTES;
  private static final int SPAWN_ITEMS_AFTER_SECONDS = 10;
  private static final int SHRINK_BORDER_AFTER_SECONDS = 60;
  private static final int WORLD_BORDER_SHRINK_SIZE = 10;
  private static final int WORLD_BORDER_SHRINK_TIME = 3;
  private static final int BLINDNESS_EFFECT_AMPLIFIER = 1;
  private static final int TITLE_FADE_IN_TIME = 0;
  private static final int TITLE_STAY_TIME = 40;
  private static final int TITLE_FADE_OUT_TIME = 0;

  private final HideAndSeekGame hideAndSeekGame;
  private int seconds;

  public HideAndSeekGameFlow(HideAndSeekGame hideAndSeekGame) {
    this.hideAndSeekGame = hideAndSeekGame;
  }

  @Override
  public void run() {
    if (!hideAndSeekGame.isRunning()) {
      cancel();
      return;
    }

    if (hideAndSeekGame.getPlayerTracker().getHiderCount() == 0) {
      announceSeekersWin();
      return;
    }

    if (seconds % SPAWN_ITEMS_AFTER_SECONDS == 0) {
      spawnRandomItemOverBorderSize();
    }

    if (seconds < HIDE_TIME_IN_MINUTES * 60) {
      applyBlindnessToSeekers();
      announceRemainingHideTime();
      hideAndSeekGame.setHideTime(true);
    } else {
      if (seconds >= TIME_IN_MINUTES * 60) {
        announceHidersWin();
      } else {
        announceRemainingGameTime();
      }

      if (hideAndSeekGame.isHideTime()) {
        removeBlindnessFromSeekers();
        hideAndSeekGame.setHideTime(false);
      }

      if (seconds % SHRINK_BORDER_AFTER_SECONDS == 0) {
        shrinkWorldBorder();
      }
    }

    seconds++;
  }

  private void announceSeekersWin() {
    Bukkit.broadcastMessage("§a§lAlle Hider wurden gefunden!");

    hideAndSeekGame
        .getPlayerTracker()
        .forEachSeeker(
            seeker -> {
              Player seekerPlayer = Bukkit.getPlayer(seeker);
              seekerPlayer.sendTitle(
                  "",
                  "§aDu hast gewonnen!",
                  TITLE_FADE_IN_TIME,
                  TITLE_STAY_TIME,
                  TITLE_FADE_OUT_TIME);
              hideAndSeekGame.getPlayerTracker().addWon(seeker);
            });

    hideAndSeekGame.stop();
    cancel();
  }

  private void shrinkWorldBorder() {
    hideAndSeekGame
        .getStartPoint()
        .getWorld()
        .getWorldBorder()
        .setSize(getWorldBorderSize() - WORLD_BORDER_SHRINK_SIZE, WORLD_BORDER_SHRINK_TIME);
  }

  private void applyBlindnessToSeekers() {
    hideAndSeekGame
        .getPlayerTracker()
        .forEachSeeker(
            seeker -> {
              Player seekerPlayer = Bukkit.getPlayer(seeker);
              seekerPlayer.addPotionEffect(
                  new PotionEffect(
                      PotionEffectType.BLINDNESS,
                      Integer.MAX_VALUE,
                      BLINDNESS_EFFECT_AMPLIFIER,
                      false,
                      false));
            });
  }

  private void announceRemainingHideTime() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              int remainingTime = HIDE_TIME_IN_MINUTES - seconds / 60;
              String message =
                  remainingTime <= 1
                      ? "§e"
                          + (HIDE_TIME_IN_MINUTES * 60 - seconds)
                          + " Sekunde(n) verbleibend zum verstecken!"
                      : "§e" + remainingTime + " Minute(n) verbleibend zum verstecken!";
              player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            });
  }

  private void announceHidersWin() {
    Bukkit.broadcastMessage("§c§lDie Zeit ist abgelaufen, die Hider haben gewonnen!");

    hideAndSeekGame
        .getPlayerTracker()
        .forEachHider(
            hider -> {
              Player hiderPlayer = Bukkit.getPlayer(hider);
              hiderPlayer.sendTitle(
                  "",
                  "§aDu hast gewonnen!",
                  TITLE_FADE_IN_TIME,
                  TITLE_STAY_TIME,
                  TITLE_FADE_OUT_TIME);
              hideAndSeekGame.getPlayerTracker().addWon(hider);
            });

    hideAndSeekGame.stop();
    cancel();
  }

  private void announceRemainingGameTime() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              String message =
                  "§e"
                      + (TIME_IN_MINUTES - seconds / 60)
                      + " Minuten und §b"
                      + hideAndSeekGame.getPlayerTracker().getHiderCount()
                      + " §eHider §b\uF102 §everbleibend!";
              player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            });
  }

  private void removeBlindnessFromSeekers() {
    hideAndSeekGame
        .getPlayerTracker()
        .forEachSeeker(
            seeker -> {
              Player seekerPlayer = Bukkit.getPlayer(seeker);
              seekerPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
            });
  }

  private double getWorldBorderSize() {
    return hideAndSeekGame.getStartPoint().getWorld().getWorldBorder().getSize();
  }

  private void spawnRandomItemOverBorderSize() {
    Location startPoint = hideAndSeekGame.getStartPoint();
    World world = startPoint.getWorld();

    double borderSize = world.getWorldBorder().getSize();
    double range = borderSize / 2;

    Random random = new Random();
    double x = startPoint.getX() + (random.nextDouble() * borderSize) - range;
    double z = startPoint.getZ() + (random.nextDouble() * borderSize) - range;

    Location itemLocation = new Location(world, x, startPoint.getY(), z);
    Location betterLocation = world.getHighestBlockAt(itemLocation).getLocation();

    ItemStack randomItem =
        HideAndSeekGame.DROPPABLE_ITEMS[random.nextInt(HideAndSeekGame.DROPPABLE_ITEMS.length)];
    Item item = world.dropItemNaturally(betterLocation, randomItem);
    item.setCustomName(randomItem.getItemMeta().getDisplayName());
    item.setCustomNameVisible(true);
    item.setGlowing(true);
    item.setPersistent(true);
    item.setUnlimitedLifetime(true);
    item.setInvulnerable(true);
  }
}
