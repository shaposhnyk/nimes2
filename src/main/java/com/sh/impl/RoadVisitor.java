package com.sh.impl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RoadVisitor<T> {
  private final Consumer<T> finishedRoadVisitor;
  private final Predicate<T> isCity;
  private final HashSet<GNode<T>> visited = new HashSet<>();

  RoadVisitor(Consumer<T> finishedRoadVisitor, Predicate<T> isCity) {
    this.finishedRoadVisitor = Objects.requireNonNull(finishedRoadVisitor);
    this.isCity = Objects.requireNonNull(isCity);
  }

  public void visitRoadsFrom(GNode<T> curr) {
    if (isCity(curr)) {
      visited.add(curr);
      for (GNode<T> next : curr.nodes()) {
        visitRoadFromCity(newRoad(), curr, next);
      }
    } else {
      visitRoadFromNonCity(newRoad(), null, curr);
    }
  }

  private List<GNode<T>> visitRoadFromNonCity(Collection<GNode<T>> path, GNode<T> prev, GNode<T> curr) {
    if (isCity(curr)) {
      visited.add(curr);
      return List.of(curr);
    } else if (isVisited(curr)) {
      if (!path.isEmpty() && path.iterator().next().equals(curr)) {
        ArrayList<GNode<T>> result = new ArrayList<>(path);
        if (prev != null) {
          result.add(prev);
        }
        result.forEach(this::finishedRoad);
        return result;
      }
      return List.of();
    }
    visited.add(curr);
    if (prev != null) {
      path.add(prev);
    }

    int roadCount = 0;
    List<GNode<T>> cityRoads = new ArrayList<>();
    for (GNode<T> next : curr.nodes()) {
      if (next.equals(prev)) {
        continue;
      }
      List<GNode<T>> road = visitRoadFromNonCity(path, curr, next);
      cityRoads.addAll(road);
      roadCount += road.isEmpty() ? 0 : 1;
    }
    if (roadCount > 1) {
      finishedRoad(curr);
      cityRoads.forEach(this::finishedRoad);
      return List.of(curr);
    }
    return List.of();
  }

  private void finishedRoad(GNode<T> curr) {
    finishedRoadVisitor.accept(curr.value());
  }

  private void visitRoadFromCity(Collection<GNode<T>> path, GNode<T> prev, GNode<T> curr) {
    if (isCity(curr)) {
      if (!path.contains(prev)) {
        finishedRoad(prev);
      }
      if (!path.contains(curr)) {
        finishedRoad(curr);
      }
      path.forEach(this::finishedRoad);
      return;
    } else if (isVisited(curr)) {
      return;
    }
    visited.add(curr);
    path.add(prev);

    for (GNode<T> next : curr.nodes()) {
      if (next.equals(prev)) {
        continue;
      }
      visitRoadFromCity(path, curr, next);
    }
  }

  private boolean isVisited(GNode<T> node) {
    return visited.contains(node);
  }

  private Collection<GNode<T>> newRoad() {
    return new LinkedHashSet<>(List.of());
  }

  public boolean isCity(GNode<T> node) {
    return isCity.test(node.value());
  }
}
