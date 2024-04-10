package dev.luzifer.hiden.listener.player;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends GameListener {

  public PlayerChatListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    handleChatDuringGame(event);
  }

  private void handleChatDuringGame(AsyncPlayerChatEvent event) {
    handleFoundChat(event);
    handleSeekerChat(event);
    handleHiderChat(event);
  }

  private void handleHiderChat(AsyncPlayerChatEvent event) {
    if (getHideAndSeekGame().getPlayerTracker().isHider(event.getPlayer().getUniqueId())) {
      event
          .getRecipients()
          .removeIf(
              player -> getHideAndSeekGame().getPlayerTracker().isSeeker(player.getUniqueId()));
      event.setFormat("§b\uF102 §r" + event.getFormat());
    }
  }

  private void handleSeekerChat(AsyncPlayerChatEvent event) {
    if (getHideAndSeekGame().getPlayerTracker().isSeeker(event.getPlayer().getUniqueId())) {
      event
          .getRecipients()
          .removeIf(
              player -> getHideAndSeekGame().getPlayerTracker().isHider(player.getUniqueId()));
      event.setFormat("§c\uF101 §r" + event.getFormat());
    }
  }

  private void handleFoundChat(AsyncPlayerChatEvent event) {
    if (getHideAndSeekGame().getPlayerTracker().isFound(event.getPlayer().getUniqueId())) {
      event
          .getRecipients()
          .removeIf(
              player -> !getHideAndSeekGame().getPlayerTracker().isFound(player.getUniqueId()));
      event.setFormat("§7[Beobachter] §r" + event.getFormat());
    }
  }
}
