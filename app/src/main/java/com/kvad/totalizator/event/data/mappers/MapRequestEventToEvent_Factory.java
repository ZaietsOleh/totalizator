// Generated by Dagger (https://dagger.dev).
package com.kvad.totalizator.event.data.mappers;

import dagger.internal.Factory;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MapRequestEventToEvent_Factory implements Factory<MapRequestEventToEvent> {
  @Override
  public MapRequestEventToEvent get() {
    return newInstance();
  }

  public static MapRequestEventToEvent_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MapRequestEventToEvent newInstance() {
    return new MapRequestEventToEvent();
  }

  private static final class InstanceHolder {
    private static final MapRequestEventToEvent_Factory INSTANCE = new MapRequestEventToEvent_Factory();
  }
}
