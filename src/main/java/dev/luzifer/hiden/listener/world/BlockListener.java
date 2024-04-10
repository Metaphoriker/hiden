package dev.luzifer.hiden.listener.world;

import dev.luzifer.hiden.game.HideAndSeekGame;
import dev.luzifer.hiden.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends GameListener {

  public BlockListener(HideAndSeekGame hideAndSeekGame) {
    super(hideAndSeekGame);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (getHideAndSeekGame().isRunning()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    if (getHideAndSeekGame().isRunning()) {
      event.setCancelled(true);
    }
  }
}
