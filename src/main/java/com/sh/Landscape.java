package com.sh;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Landscape representing map of tiles
 */
public interface Landscape<T> {
  @NotNull Map<Direction, Point> sides();

  @NotNull Direction opposide(Direction dir);

  @NotNull List<Point> frontier();

  @NotNull List<T> field();

  T get(@NotNull Point p);

  void add(@NotNull Point p, T ch);
}
