package dev.luzifer.hiden.commands;

import dev.luzifer.hiden.game.HideAndSeekGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

  private final HideAndSeekGame hideAndSeekGame;

  public SpawnCommand(HideAndSeekGame hideAndSeekGame) {
    this.hideAndSeekGame = hideAndSeekGame;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] strings) {
    if (commandSender instanceof Player player) {
      if (!hideAndSeekGame.isRunning()) {
        player.teleport(hideAndSeekGame.getStartPoint());
        return true;
      } else if (!hideAndSeekGame.getPlayerTracker().isHider(player.getUniqueId())) {
        player.teleport(hideAndSeekGame.getStartPoint());
        player.sendMessage("§aDu wurdest zum Startpunkt teleportiert.");
        return true;
      }
    }
    return false;
  }
}
