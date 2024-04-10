package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerHitListener extends GameListener {

  public PlayerHitListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
    hideAndSeekGame.addEndListener(this::clearLinkage);
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player player && event.getDamager() instanceof Player) {
      handlePlayerHit(event, player);
    }
  }

  private void clearLinkage() {
    getHideAndSeekGame().getLinkageManager().getLinkageMap().clear();
    getHideAndSeekGame().getLinkageManager().getLinkedRequestMap().clear();
  }

  private boolean isLinked(Player a, Player b) {
    return getHideAndSeekGame().getLinkageManager().getLinkageMap().containsKey(a.getUniqueId())
        && getHideAndSeekGame()
            .getLinkageManager()
            .getLinkageMap()
            .get(a.getUniqueId())
            .equals(b.getUniqueId());
  }

  private void handlePlayerHit(EntityDamageByEntityEvent event, Player player) {
    event.setDamage(0);

    if (getHideAndSeekGame().isHideTime()) {
      player.sendMessage("§cDu kannst in der Versteckzeit niemanden schlagen!");
      event.setCancelled(true);
      return;
    }

    Player damager = (Player) event.getDamager();
    handleItemInteraction(player, damager);

    if (getHideAndSeekGame().getPlayerTracker().isHider(player.getUniqueId())
        && getHideAndSeekGame().getPlayerTracker().isSeeker(damager.getUniqueId())) {
      handleHiderFound(player, damager, event);
    }
  }

  private void handleItemInteraction(Player player, Player damager) {
    ItemStack damagerItem = damager.getInventory().getItemInMainHand();
    if (damagerItem != null && damagerItem.getType() != Material.AIR) {
      if (damagerItem.isSimilar(HideAndSeekGame.LIGHT_STICK_ITEM)) {
        revealPlayer(player, damager, damagerItem);
      } else if (damagerItem.isSimilar(HideAndSeekGame.LINKAGE_ITEM)) {
        linkPlayers(player, damager);
      }
    }
  }

  private void revealPlayer(Player player, Player damager, ItemStack damagerItem) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 10, 1));
    player.sendTitle(
        "§6Friendly Fire", "§eDu wurdest von §6" + damager.getName() + " §eoffenbart!", 0, 40, 0);
    player
        .getLocation()
        .getWorld()
        .playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 3);

    damagerItem.setAmount(damagerItem.getAmount() - 1);
  }

  private void linkPlayers(Player player, Player damager) {
    if (getHideAndSeekGame()
            .getLinkageManager()
            .getLinkedRequestMap()
            .containsKey(damager.getUniqueId())
        && getHideAndSeekGame()
            .getLinkageManager()
            .getLinkedRequestMap()
            .get(damager.getUniqueId())
            .equals(player.getUniqueId())) {
      establishLinkBetweenPlayers(player, damager);
    } else {
      sendLinkRequest(player, damager);
    }
  }

  private void establishLinkBetweenPlayers(Player player, Player damager) {
    getHideAndSeekGame()
        .getLinkageManager()
        .getLinkageMap()
        .put(damager.getUniqueId(), player.getUniqueId());
    getHideAndSeekGame()
        .getLinkageManager()
        .getLinkageMap()
        .put(player.getUniqueId(), damager.getUniqueId());
    getHideAndSeekGame().getLinkageManager().getLinkedRequestMap().remove(damager.getUniqueId());
    getHideAndSeekGame().getLinkageManager().getLinkedRequestMap().remove(player.getUniqueId());

    damager.sendMessage("§aDu hast dich mit " + player.getName() + " verlinkt.");
    player.sendMessage("§aDu hast dich mit " + damager.getName() + " verlinkt.");
  }

  private void sendLinkRequest(Player player, Player damager) {
    if (isLinked(damager, player)) {
      damager.sendMessage("§cDu bist bereits mit " + player.getName() + " verlinkt.");
      return;
    }

    getHideAndSeekGame()
        .getLinkageManager()
        .getLinkedRequestMap()
        .put(player.getUniqueId(), damager.getUniqueId());
    damager.sendMessage("§aDu hast eine Verlinkungsanfrage an " + player.getName() + " gesendet.");
    player.sendMessage("§aDu hast eine Verlinkungsanfrage von " + damager.getName() + " erhalten.");
  }

  private void handleHiderFound(Player player, Player damager, EntityDamageByEntityEvent event) {
    if (hasKevlarVest(player)) {
      damager.sendMessage("§cDer Spieler wurde durch eine Kevlarweste geschützt!");
      event.setCancelled(true);
      player.getInventory().setChestplate(null);
      return;
    }

    setFound(player);
    broadcastPlayerFoundByMessage(player, damager);

    if (getHideAndSeekGame().getPlayerTracker().getHiderCount() == 0) {
      endGame();
    }
  }

  private void broadcastPlayerFoundByMessage(Player player, Player damager) {
    Bukkit.broadcastMessage(
        "§c"
            + player.getName()
            + " wurde von "
            + damager.getName()
            + " gefunden! "
            + "§7[§c"
            + getHideAndSeekGame().getPlayerTracker().getFoundCount()
            + "§7/§b"
            + getHideAndSeekGame().getPlayerTracker().getHiderCount()
            + "§7]");
  }

  private void endGame() {
    Bukkit.broadcastMessage("§a§lAlle Hider wurden gefunden!");

    getHideAndSeekGame()
        .getPlayerTracker()
        .forEachSeeker(
            seeker -> {
              Player seekerPlayer = Bukkit.getPlayer(seeker);
              seekerPlayer.sendTitle("", "§aDu hast gewonnen!", 0, 40, 0);

              getHideAndSeekGame().getPlayerTracker().addWon(seeker);
            });

    getHideAndSeekGame().stop();
    Bukkit.getScheduler()
        .runTaskLater(HidenPlugin.getInstance(), getHideAndSeekGame()::start, 20 * 5);
  }

  private boolean hasKevlarVest(Player player) {
    if (player.getInventory().getChestplate() == null) return false;
    return player.getInventory().getChestplate().isSimilar(HideAndSeekGame.KEVLAR_VEST_ITEM);
  }
}
