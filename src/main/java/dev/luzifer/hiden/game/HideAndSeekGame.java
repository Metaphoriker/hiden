package dev.luzifer.hiden.game;

import dev.luzifer.hiden.HidenPlugin;
import dev.luzifer.hiden.items.itemhandler.ItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.BoostItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.DecoyGrenadeItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.DisappearItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.FlashGrenadeItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.GiveUpItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.InvisibilityItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.LaserGrenadeItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.LightStickItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.LinkageItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.MouseMouseBeepOnceItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.PepperSprayItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.RandomTeleportItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.SacrificialLambItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.SmokeGrenadeItemHandler;
import dev.luzifer.hiden.items.itemhandler.impl.SpeedAndJumpHeightItemHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// TODO: cleanup
public class HideAndSeekGame {

  public static final ItemStack AUFGEBEN_ITEM = new ItemStack(Material.BARRIER);

  public static final ItemStack FLASH_GRENADE_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack SMOKE_GRENADE_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack LASER_GRENADE_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack DECOY_GRENADE_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack LIGHT_STICK_ITEM = new ItemStack(Material.BLAZE_ROD);
  public static final ItemStack PEPPER_SPRAY_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack BOOST_ITEM = new ItemStack(Material.FEATHER);
  public static final ItemStack MAUESSCHEN_MAUESSCHEN_PIEP_EINMAL_ITEM =
      new ItemStack(Material.PAPER);
  public static final ItemStack KEVLAR_VEST_ITEM = new ItemStack(Material.LEATHER_CHESTPLATE);
  public static final ItemStack LINKAGE_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack RANDOM_TELEPORT_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack SPEED_AND_JUMP_HEIGHT_ITEM = new ItemStack(Material.FEATHER);
  public static final ItemStack SACRIFICIAL_LAMB_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack DISAPPEAR_FROM_THE_RADAR_ITEM = new ItemStack(Material.PAPER);
  public static final ItemStack INVISIBILITY_ITEM = new ItemStack(Material.PAPER);

  public static final ItemStack[] DROPPABLE_ITEMS = {
    FLASH_GRENADE_ITEM,
    SMOKE_GRENADE_ITEM,
    PEPPER_SPRAY_ITEM,
    DECOY_GRENADE_ITEM,
    LIGHT_STICK_ITEM,
    KEVLAR_VEST_ITEM,
    LINKAGE_ITEM,
    RANDOM_TELEPORT_ITEM,
    SPEED_AND_JUMP_HEIGHT_ITEM,
    SACRIFICIAL_LAMB_ITEM,
    INVISIBILITY_ITEM
  };

  public static final ItemStack[] ALL_ITEMS = {
    AUFGEBEN_ITEM,
    FLASH_GRENADE_ITEM,
    SMOKE_GRENADE_ITEM,
    LASER_GRENADE_ITEM,
    PEPPER_SPRAY_ITEM,
    DECOY_GRENADE_ITEM,
    LIGHT_STICK_ITEM,
    MAUESSCHEN_MAUESSCHEN_PIEP_EINMAL_ITEM,
    BOOST_ITEM,
    KEVLAR_VEST_ITEM,
    LINKAGE_ITEM,
    RANDOM_TELEPORT_ITEM,
    SPEED_AND_JUMP_HEIGHT_ITEM,
    SACRIFICIAL_LAMB_ITEM,
    DISAPPEAR_FROM_THE_RADAR_ITEM,
    INVISIBILITY_ITEM
  };

  private static final Map<ItemStack, ItemData> itemDataMap = new HashMap<>();
  private static final Map<ItemStack, ItemHandler> itemListener = new HashMap<>();

  static {
    itemDataMap.put(
        AUFGEBEN_ITEM,
        new ItemData(
            "§c§lAufgeben §7(Rechtsklick)",
            Material.BARRIER,
            null,
            List.of("§7§oBei Betätigung wirst du gefunden und scheidest aus.", "§9§oHIDER ITEM")));
    itemDataMap.put(
        FLASH_GRENADE_ITEM,
        new ItemData(
            "§f§lBlendgranate §7(Rechtsklick)",
            Material.PAPER,
            1004,
            List.of("§7§oWirft eine Blendgranate die alle Spieler im Umkreis erblinden lässt.")));
    itemDataMap.put(
        SMOKE_GRENADE_ITEM,
        new ItemData(
            "§8§lRauchgranate §7(Rechtsklick)",
            Material.PAPER,
            1005,
            List.of(
                "§7§oWirft eine Rauchgranate, welche eine stetig wachsende Rauchschwade erzeugt.")));
    itemDataMap.put(
        LASER_GRENADE_ITEM,
        new ItemData(
            "§c§lLasergranate §7(Rechtsklick)",
            Material.PAPER,
            1007,
            List.of(
                "§7§oWirft eine Lasergranate, welche alle Hider im Umkreis offenbart.",
                "§c§oSEEKER ITEM")));
    itemDataMap.put(
        PEPPER_SPRAY_ITEM,
        new ItemData(
            "§6§lPfefferspray §7(Rechtsklick)",
            Material.PAPER,
            1009,
            List.of(
                "§7§oSprüht Pfefferspray, welches das anvisierte Ziel verlangsamt und kurzzeitig dessen Sicht einschränkt.")));
    itemDataMap.put(
        DECOY_GRENADE_ITEM,
        new ItemData(
            "§6§lAttrappe §7(Rechtsklick)",
            Material.PAPER,
            1006,
            List.of("§7§oWirft eine Attrappe, welche Spielerschritte simuliert.")));
    itemDataMap.put(
        LIGHT_STICK_ITEM,
        new ItemData(
            "§e§lKnicklicht §7(Rechtsklick)",
            Material.BLAZE_ROD,
            null,
            List.of(
                "§7§oOffenbart dich für 10 Sekunden.",
                "§7§oDu kannst damit auch andere Spieler offenbaren indem du sie schlägst.")));
    itemDataMap.put(
        MAUESSCHEN_MAUESSCHEN_PIEP_EINMAL_ITEM,
        new ItemData(
            "§6§lMäuschen, Mäuschen, piep einmal §7(Rechtsklick)",
            Material.PAPER,
            1008,
            List.of("§7§oLässt den nächsten Hider erklingen.", "§c§oSUCHER ITEM")));
    itemDataMap.put(
        BOOST_ITEM,
        new ItemData(
            "§e§lBoost §7(Rechtsklick)",
            Material.FEATHER,
            null,
            List.of(
                "§7§oBoosted dich in die Richtung, in die du schaust.",
                "§7§oTIPP: Springen verleiht dem Effekt mehr Effizienz.",
                "§c§oSUCHER ITEM")));
    itemDataMap.put(
        KEVLAR_VEST_ITEM,
        new ItemData(
            "§7§lKevlarweste §7(Anziehen)",
            Material.LEATHER_CHESTPLATE,
            null,
            List.of("§7§oBeschützt dich vor einem Treffer.", "§9§oHIDER ITEM")));
    itemDataMap.put(
        LINKAGE_ITEM,
        new ItemData(
            "§6§lVerknüpfung §7(Rechtsklick)",
            Material.PAPER,
            1010,
            List.of(
                "§7§oWenn zwei Spieler dieses Item haben, können sie sich gegenseitig damit schlagen und somit miteinander verlinken.",
                "§7§oSollte das Item von einem Spieler benutzt werden, wird dieser Spieler zum anderen teleportiert und beide Items aufgebraucht.")));
    itemDataMap.put(
        RANDOM_TELEPORT_ITEM,
        new ItemData(
            "§6§lZufällige Teleportation §7(Rechtsklick)",
            Material.PAPER,
            1011,
            List.of("§7§oTeleportiert dich an eine zufällige Position im Umkreis.")));
    itemDataMap.put(
        SPEED_AND_JUMP_HEIGHT_ITEM,
        new ItemData(
            "§6§lGeschwindigkeit und Sprunghöhe §7(Rechtsklick)",
            Material.FEATHER,
            null,
            List.of("§7§oErhöht deine Geschwindigkeit und Sprunghöhe für 5 Sekunden.")));
    itemDataMap.put(
        SACRIFICIAL_LAMB_ITEM,
        new ItemData(
            "§6§lOpferlamm §7(Rechtsklick)",
            Material.PAPER,
            1012,
            List.of("§7§oWechselt die Position mit einem anderen Hider.")));
    itemDataMap.put(
        DISAPPEAR_FROM_THE_RADAR_ITEM,
        new ItemData(
            "§6§lVom Radar verschwinden §7(Rechtsklick)",
            Material.PAPER,
            1013,
            List.of("§7§oVerschwinde für 20 Sekunden vom Radar der Hider.", "§c§oSUCHER ITEM")));
    itemDataMap.put(
        INVISIBILITY_ITEM,
        new ItemData(
            "§6§lUnsichtbarkeit §7(Rechtsklick)",
            Material.PAPER,
            1014,
            List.of("§7§oMacht dich für 10 Sekunden unsichtbar.")));

    for (Map.Entry<ItemStack, ItemData> entry : itemDataMap.entrySet()) {
      setItemMeta(entry.getKey(), entry.getValue());
    }
  }

  private static void setItemMeta(ItemStack item, ItemData itemData) {
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(itemData.name());
    itemMeta.setLore(itemData.lore());
    if (itemData.customModelData() != null) {
      itemMeta.setCustomModelData(itemData.customModelData());
    }
    item.setItemMeta(itemMeta);
  }

  private void registerItemHandler() {
    itemListener.put(AUFGEBEN_ITEM, new GiveUpItemHandler(this));
    itemListener.put(FLASH_GRENADE_ITEM, new FlashGrenadeItemHandler(this));
    itemListener.put(SMOKE_GRENADE_ITEM, new SmokeGrenadeItemHandler(this));
    itemListener.put(LASER_GRENADE_ITEM, new LaserGrenadeItemHandler(this));
    itemListener.put(DECOY_GRENADE_ITEM, new DecoyGrenadeItemHandler(this));
    itemListener.put(LIGHT_STICK_ITEM, new LightStickItemHandler(this));
    itemListener.put(PEPPER_SPRAY_ITEM, new PepperSprayItemHandler(this));
    itemListener.put(BOOST_ITEM, new BoostItemHandler(this));
    itemListener.put(
        MAUESSCHEN_MAUESSCHEN_PIEP_EINMAL_ITEM, new MouseMouseBeepOnceItemHandler(this));
    itemListener.put(LINKAGE_ITEM, new LinkageItemHandler(this));
    itemListener.put(RANDOM_TELEPORT_ITEM, new RandomTeleportItemHandler(this));
    itemListener.put(SPEED_AND_JUMP_HEIGHT_ITEM, new SpeedAndJumpHeightItemHandler(this));
    itemListener.put(SACRIFICIAL_LAMB_ITEM, new SacrificialLambItemHandler(this));
    itemListener.put(DISAPPEAR_FROM_THE_RADAR_ITEM, new DisappearItemHandler(this));
    itemListener.put(INVISIBILITY_ITEM, new InvisibilityItemHandler(this));
  }

  private record ItemData(
      String name, Material material, Integer customModelData, List<String> lore) {}

  @Getter private final PlayerTracker playerTracker = new PlayerTracker();
  @Getter private final LinkageManager linkageManager = new LinkageManager();
  private final List<Runnable> endListener = new ArrayList<>();

  @Getter @Setter private Location startPoint;
  @Getter @Setter private Location lobbyPoint;
  @Getter private boolean running;
  @Setter @Getter private boolean hideTime;

  public HideAndSeekGame() {
    registerItemHandler();
  }

  public void handleItem(Player player, ItemStack itemStack) {
    ItemHandler itemHandler = itemListener.get(itemStack);
    if (itemHandler == null) return;
    itemHandler.handleItem(player, itemStack);
  }

  public void start() {
    announceGameStart();
    resetPlayerEffects();
    prepareWorldBorder();
    preparePlayers();
    decideSeekers();
    startGameFlow();
    playerTracker.clearOther();
  }

  private void announceGameStart() {
    Bukkit.broadcastMessage("§7Das Spiel beginnt, die §cVersteckzeit §7hat begonnen!");
  }

  private void resetPlayerEffects() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              player.setGlowing(false);
              player.setWalkSpeed(0.2f);
            });
  }

  private void prepareWorldBorder() {
    startPoint.getWorld().getWorldBorder().setCenter(startPoint);
    startPoint.getWorld().getWorldBorder().setSize(500);
    startPoint.getWorld().getWorldBorder().setDamageAmount(1);
    startPoint.getWorld().getWorldBorder().setDamageBuffer(1);
  }

  private void preparePlayers() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              playerTracker.setHiderState(player.getUniqueId());
              player.teleport(startPoint);
              player.setGameMode(GameMode.SURVIVAL);
              player.getInventory().clear();
            });
  }

  private void decideSeekers() {
    int seekerAmountBasedOnHiders = playerTracker.getHiderCount() / 4;
    seekerAmountBasedOnHiders = Math.max(1, seekerAmountBasedOnHiders);
    evaluateSeekerPreference();
    int amountOfSeekersStillNeeded = seekerAmountBasedOnHiders - playerTracker.getSeekerCount();

    if (amountOfSeekersStillNeeded != 0) {
      if (playerTracker.getFoundCount() == 0) {
        decideFirstSeekers(amountOfSeekersStillNeeded);
      } else {
        decideNextSeekers(amountOfSeekersStillNeeded);
      }
    }

    setSeekersGlowing();
  }

  private void startGameFlow() {
    playerTracker.forEachSeeker(
        seeker -> {
          Bukkit.broadcastMessage(
              "§c" + Bukkit.getOfflinePlayer(seeker).getName() + " ist ein Sucher!");
        });

    playerTracker.forEachHider(
        hider -> {
          Player player = Bukkit.getPlayer(hider);
          player.getInventory().setItem(8, AUFGEBEN_ITEM);
        });

    hideAllHiderNameTagsForSeekerAndViceVersa();

    running = true;

    new HideAndSeekGameFlow(this).runTaskTimer(HidenPlugin.getInstance(), 1, 20);
  }

  private void evaluateSeekerPreference() {
    int amountOfSeekers = playerTracker.getHiderCount() / 4;
    int seekerAmountOrOne = Math.max(1, amountOfSeekers);

    for (int i = 0; i < seekerAmountOrOne; i++) {
      if (playerTracker.getSeekerPreferenceCount() == 0) {
        break;
      }
      int random = (int) (Math.random() * playerTracker.getSeekerPreferenceCount());
      UUID seekerPreference = playerTracker.getSeekerPreference(random);
      playerTracker.setSeekerState(seekerPreference);
      playerTracker.removeSeekerPreference(seekerPreference);
    }
  }

  private void resetGlowEffects() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              player.setGlowing(false);
              player.setWalkSpeed(0.2f);
            });
  }

  private void setSeekersGlowing() {
    playerTracker.forEachSeeker(
        seeker -> {
          Player player = Bukkit.getPlayer(seeker);
          player.setGlowing(true);
          player.setWalkSpeed((player.getWalkSpeed() * 1.5f));

          addItemToInventory(player, LASER_GRENADE_ITEM, 5);
          addItemToInventory(player, MAUESSCHEN_MAUESSCHEN_PIEP_EINMAL_ITEM, 3);
          addItemToInventory(player, DISAPPEAR_FROM_THE_RADAR_ITEM, 6);

          player.getInventory().setItem(8, BOOST_ITEM);

          player.sendMessage(
              "§eMit §6/spawn §ekommst du zurück zum Spawn. §7Das ist nur für §cSucher §7möglich!");
        });
  }

  private void addItemToInventory(Player player, ItemStack item, int amount) {
    ItemStack clonedItem = item.clone();
    clonedItem.setAmount(amount);
    player.getInventory().addItem(clonedItem);
  }

  private void showAllPlayer() {
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              handleFlying(player);
              Bukkit.getOnlinePlayers().forEach(player::showPlayer);
            });
  }

  private void handleFlying(Player player) {
    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
      player.setAllowFlight(false);
      player.setFlying(false);
    }
  }

  public void stop() {
    resetGameState();
    resetPlayerStates();
    removeEntities();
    notifyEndListeners();
  }

  private void resetGameState() {
    playerTracker.clearGameRelevant();
    running = false;
    resetGlowEffects();
    showNameTags();
    setHideTime(false);
    showAllPlayer();
  }

  private void resetPlayerStates() {
    Bukkit.getOnlinePlayers().forEach(this::resetPlayerState);
  }

  private void resetPlayerState(Player player) {
    clearPlayerPotionEffects(player);
    player.getInventory().clear();
    teleportPlayerToLobby(player);
    player.setGameMode(GameMode.ADVENTURE);
  }

  private void clearPlayerPotionEffects(Player player) {
    player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
  }

  private void teleportPlayerToLobby(Player player) {
    Bukkit.getScheduler().runTask(HidenPlugin.getInstance(), () -> player.teleport(lobbyPoint));
  }

  private void removeEntities() {
    List<Entity> entitiesToRemove = getDroppableItems();
    entitiesToRemove.forEach(Entity::remove);
  }

  private List<Entity> getDroppableItems() {
    return startPoint.getWorld().getEntities().stream().filter(Item.class::isInstance).toList();
  }

  private void notifyEndListeners() {
    endListener.forEach(Runnable::run);
  }

  public void addEndListener(Runnable runnable) {
    endListener.add(runnable);
  }

  public void decideFirstSeekers(int amount) {
    for (int i = 0; i < amount; i++) {
      int random = (int) (Math.random() * playerTracker.getHiderCount());
      playerTracker.setSeekerState(playerTracker.getHider(random));
    }
  }

  public void decideNextSeekers(int amount) {
    List<UUID> seeker = new ArrayList<>();
    playerTracker.forEachHider(
        hider -> {
          if (seeker.size() >= amount) return;
          seeker.add(hider);
        });
    seeker.forEach(playerTracker::setSeekerState);
  }

  private void hideAllHiderNameTagsForSeekerAndViceVersa() {
    NameTagManager nameTagManager = TabAPI.getInstance().getNameTagManager();
    playerTracker.forEachHider(hider -> hideNameTagsForPlayer(hider, nameTagManager));
    setWonPrefix(nameTagManager);
  }

  private void hideNameTagsForPlayer(UUID hider, NameTagManager nameTagManager) {
    Player hiderPlayer = Bukkit.getPlayer(hider);
    TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(hiderPlayer.getUniqueId());

    setPlayerPrefix(tabPlayer, "§b\uF101 ");

    playerTracker.forEachSeeker(
        seeker -> {
          Player seekerPlayer = Bukkit.getPlayer(seeker);
          TabPlayer tabSeekerPlayer = TabAPI.getInstance().getPlayer(seekerPlayer.getUniqueId());

          nameTagManager.hideNameTag(tabPlayer, tabSeekerPlayer);
          nameTagManager.hideNameTag(tabSeekerPlayer, tabPlayer);

          setPlayerPrefix(tabSeekerPlayer, "§c§l\uF102 ");
        });
  }

  private void setPlayerPrefix(TabPlayer player, String prefix) {
    NameTagManager nameTagManager = TabAPI.getInstance().getNameTagManager();
    nameTagManager.setPrefix(player, prefix);
    TabAPI.getInstance().getTabListFormatManager().setPrefix(player, prefix);
  }

  private void setWonPrefix(NameTagManager nameTagManager) {
    playerTracker.forEachWon(
        wonPlayer -> {
          Player player = Bukkit.getPlayer(wonPlayer);
          TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
          String prefix = "§6\uF103 " + nameTagManager.getCustomPrefix(tabPlayer);
          setPlayerPrefix(tabPlayer, prefix);
        });
  }

  private void showNameTags() {
    NameTagManager nameTagManager = TabAPI.getInstance().getNameTagManager();
    Bukkit.getOnlinePlayers().forEach(player -> showNameTagsForPlayer(player, nameTagManager));
    setWonPrefix(nameTagManager);
  }

  private void showNameTagsForPlayer(Player player, NameTagManager nameTagManager) {
    TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

    Bukkit.getOnlinePlayers()
        .forEach(
            onlinePlayer -> {
              TabPlayer tabOnlinePlayer =
                  TabAPI.getInstance().getPlayer(onlinePlayer.getUniqueId());
              nameTagManager.showNameTag(tabPlayer, tabOnlinePlayer);
              nameTagManager.showNameTag(tabOnlinePlayer, tabPlayer);

              setPlayerPrefix(tabPlayer, "§f");
              setPlayerPrefix(tabOnlinePlayer, "§f");
            });
  }
}