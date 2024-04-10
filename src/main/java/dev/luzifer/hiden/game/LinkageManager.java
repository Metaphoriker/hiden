package dev.luzifer.hiden.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

// TODO: encapsulate
public class LinkageManager {
  @Getter private final Map<UUID, UUID> linkageMap = new HashMap<>();
  @Getter private final Map<UUID, UUID> linkedRequestMap = new HashMap<>();
}
