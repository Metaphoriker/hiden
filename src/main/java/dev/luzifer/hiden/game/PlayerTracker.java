package dev.luzifer.hiden.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerTracker {

  private final List<UUID> seekers = new ArrayList<>();
  private final List<UUID> hiders = new ArrayList<>();
  private final List<UUID> found = new ArrayList<>();
  private final List<UUID> won = new ArrayList<>();
  private final List<UUID> seekerPreference = new ArrayList<>();

  public boolean isFound(UUID uuid) {
    return found.contains(uuid);
  }

  public boolean isSeeker(UUID uuid) {
    return seekers.contains(uuid);
  }

  public boolean isHider(UUID uuid) {
    return hiders.contains(uuid);
  }

  public void setFoundState(UUID uuid) {
    found.add(uuid);
    seekers.remove(uuid);
    hiders.remove(uuid);
  }

  public void setSeekerState(UUID uuid) {
    seekers.add(uuid);
    hiders.remove(uuid);
  }

  public void setHiderState(UUID uuid) {
    hiders.add(uuid);
    seekers.remove(uuid);
  }

  public void addWon(UUID uuid) {
    won.add(uuid);
  }

  public void addSeekerPreference(UUID uuid) {
    seekerPreference.add(uuid);
  }

  public void removeSeekerPreference(UUID uuid) {
    seekerPreference.remove(uuid);
  }

  public UUID getSeekerPreference(int index) {
    return seekerPreference.get(index);
  }

  public UUID getHider(int index) {
    return hiders.get(index);
  }

  public UUID getSeeker(int index) {
    return seekers.get(index);
  }

  public UUID getFound(int index) {
    return found.get(index);
  }

  public UUID getWon(int index) {
    return won.get(index);
  }

  public void removeSeeker(UUID uuid) {
    seekers.remove(uuid);
  }

  public void removeHider(UUID uuid) {
    hiders.remove(uuid);
  }

  public boolean hasPreference(UUID uuid) {
    return seekerPreference.contains(uuid);
  }

  public void clearGameRelevant() {
    seekers.clear();
    hiders.clear();
    found.clear();
  }

  public void clearOther() {
    won.clear();
    seekerPreference.clear();
  }

  public void clearAll() {
    seekers.clear();
    hiders.clear();
    found.clear();
    won.clear();
    seekerPreference.clear();
  }

  public void forEachSeeker(Consumer<UUID> action) {
    seekers.forEach(action);
  }

  public void forEachHider(Consumer<UUID> action) {
    hiders.forEach(action);
  }

  public void forEachFound(Consumer<UUID> action) {
    found.forEach(action);
  }

  public void forEachWon(Consumer<UUID> action) {
    won.forEach(action);
  }

  public void forEachSeekerPreference(Consumer<UUID> action) {
    seekerPreference.forEach(action);
  }

  public int getSeekerCount() {
    return seekers.size();
  }

  public int getHiderCount() {
    return hiders.size();
  }

  public int getFoundCount() {
    return found.size();
  }

  public int getWonCount() {
    return won.size();
  }

  public int getSeekerPreferenceCount() {
    return seekerPreference.size();
  }
}
