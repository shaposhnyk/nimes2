package com.sh.impl;

import com.sh.Direction;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public record Frontier<T>(T element, Set<Direction> directions) {
  public Frontier(T ch, Direction... directions) {
    this(ch, new LinkedHashSet<>(Arrays.asList(directions)));
  }

  Frontier<T> withSide(Direction direction) {
    return new Frontier<>(element, direction);
  }

  Frontier<T> withoutSide(Direction direction) {
    LinkedHashSet<Direction> newSet = new LinkedHashSet<>(directions);
    if (!newSet.remove(direction)) {
      throw new IllegalArgumentException("Expecting to have a side: " + direction + ", but  was: " + this.directions);
    }
    return new Frontier<>(element, newSet);
  }

  boolean isEmpty() {
    return directions.isEmpty();
  }

  public Frontier<T> withFirstSide() {
    return new Frontier<>(element, firstSide());
  }

  public Direction firstSide() {
    return directions.iterator().next();
  }

  public String asCanonicalString() {
    return element.toString() + directions;
  }

  public boolean hasSide(Direction direction) {
    return directions.contains(direction);
  }
}
