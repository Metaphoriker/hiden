package dev.luzifer.hiden.commands;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.menu.ItemInventory;
import dev.luzifer.hiden.menu.PreferenceInventory;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HideCommand implements TabExecutor {

  private static final String[] SUB_COMMANDS = {
    "start", "stop", "setspawn", "setlobby", "items", "preference"
  };

  private final HidenPlugin instance;
  private final HideAndSeekGame hideAndSeekGame;

  public HideCommand(HidenPlugin instance, HideAndSeekGame hideAndSeekGame) {
    this.instance = instance;
    this.hideAndSeekGame = hideAndSeekGame;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cDu musst ein Spieler sein!");
      return true;
    }

    if (args.length < 1) {
      displayAvailableCommands(sender);
      return true;
    }

    switch (args[0].toLowerCase()) {
      case "start":
        handleStartCommand(sender);
        break;
      case "stop":
        handleStopCommand(sender);
        break;
      case "setspawn":
        handleSetSpawnCommand(sender);
        break;
      case "items":
        handleItemsCommand(sender);
        break;
      case "setlobby":
        handleSetLobbyCommand(sender);
        break;
      case "preference":
        handlePreferenceCommand(sender);
        break;
      default:
        sender.sendMessage("§cUnbekanntes Argument!");
        break;
    }

    return false;
  }

  private void displayAvailableCommands(CommandSender sender) {
    sender.sendMessage("§7Folgende Argumente sind verfügbar:");
    for (String subCommand : SUB_COMMANDS) {
      sender.sendMessage("§8- §e" + subCommand);
    }
  }

  private void handleStartCommand(CommandSender sender) {
    if (!sender.isOp()) {
      sender.sendMessage("§cDu hast keine Berechtigung dazu!");
      return;
    }

    if (hideAndSeekGame.getStartPoint() == null) {
      sender.sendMessage("§cDu musst zuerst den Spawn setzen!");
      return;
    }
    if (hideAndSeekGame.isRunning()) {
      sender.sendMessage("§cDas Spiel läuft bereits!");
      return;
    }
    hideAndSeekGame.start();
    sender.sendMessage("§aHide and Seek gestartet!");
  }

  private void handleStopCommand(CommandSender sender) {
    if (!sender.isOp()) {
      sender.sendMessage("§cDu hast keine Berechtigung dazu!");
      return;
    }

    hideAndSeekGame.stop();
    sender.sendMessage("§cHide and Seek gestoppt!");
  }

  private void handleSetSpawnCommand(CommandSender sender) {
    if (!sender.isOp()) {
      sender.sendMessage("§cDu hast keine Berechtigung dazu!");
      return;
    }

    hideAndSeekGame.setStartPoint(((Player) sender).getLocation());
    sender.sendMessage("§aSpawn point gesetzt!");
    instance.saveSpawn();
  }

  private void handleItemsCommand(CommandSender sender) {
    ItemInventory itemInventory = new ItemInventory();
    ((Player) sender).openInventory(itemInventory.getInventory());
    sender.sendMessage("§aItem-Menu geöffnet!");
  }

  private void handleSetLobbyCommand(CommandSender sender) {
    if (!sender.isOp()) {
      sender.sendMessage("§cDu hast keine Berechtigung dazu!");
      return;
    }

    hideAndSeekGame.setLobbyPoint(((Player) sender).getLocation());
    sender.sendMessage("§aLobby point gesetzt!");
    instance.saveLobby();
  }

  private void handlePreferenceCommand(CommandSender sender) {
    PreferenceInventory preferenceInventory = new PreferenceInventory(hideAndSeekGame);
    ((Player) sender).openInventory(preferenceInventory.getInventory());
  }

  @Nullable
  @Override
  public List<String> onTabComplete(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] strings) {
    return List.of(SUB_COMMANDS);
  }
}
