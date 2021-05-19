package com.sh;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Landscape representing map of tiles,
 * capable to return a set of points where next elements can be set
 */
public interface Landscape<T> {
  /**
   * Adds new element at a given point
   *
   * @throws IllegalArgumentException if point is already occupied
   */
  void add(@NotNull Point p, @NotNull T element);

  /**
   * @return element at point if exists or null otherwise
   */
  T get(@NotNull Point p);

  /**
   * @return list of all items on the landscape
   */
  @NotNull List<T> elements();

  /**
   * @return list of all points on a frontier, where new item can be added
   */
  @NotNull List<Point> frontier();

  /**
   * @return opposite direction for a given one
   */
  @NotNull Direction opposite(@NotNull Direction dir);

  @NotNull Map<Direction, Point> sides();
}
