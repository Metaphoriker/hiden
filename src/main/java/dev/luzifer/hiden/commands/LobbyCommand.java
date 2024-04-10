package dev.luzifer.hiden.commands;

import dev.luzifer.hiden.game.HideAndSeekGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand implements CommandExecutor {

  private final HideAndSeekGame hideAndSeekGame;

  public LobbyCommand(HideAndSeekGame hideAndSeekGame) {
    this.hideAndSeekGame = hideAndSeekGame;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] strings) {

    if (hideAndSeekGame.isRunning()) {
      commandSender.sendMessage("§cWährend des Spiels kannst du nicht in die Lobby zurückkehren!");
      return true;
    }

    if (commandSender instanceof Player player) {
      player.teleport(hideAndSeekGame.getLobbyPoint());
      player.sendMessage("§aDu wurdest zur Lobby teleportiert.");
      return true;
    }

    return false;
  }
}
