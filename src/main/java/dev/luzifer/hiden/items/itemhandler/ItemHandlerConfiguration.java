package dev.luzifer.hiden.items.itemhandler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

@With
@Value
@Getter
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemHandlerConfiguration {

  @Builder.Default boolean cooldown = true;
  @Builder.Default boolean reduceItem = true;

  public static ItemHandlerConfiguration createDefault() {
    return ItemHandlerConfiguration.builder().build();
  }

  public static ItemHandlerConfiguration createOppositeToDefault() {
    return ItemHandlerConfiguration.builder().cooldown(false).reduceItem(false).build();
  }
}
