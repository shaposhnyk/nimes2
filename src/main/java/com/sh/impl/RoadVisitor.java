package com.sh.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RoadVisitor<T> {
  private static final Logger logger = LoggerFactory.getLogger(RoadVisitor.class);
  private final Predicate<T> isCity;
  private final Consumer<T> finishedRoadVisitor;

  private final Set<GNode<T>> visited = new HashSet<>();
  private final Set<GNode<T>> marked = new HashSet<>();
  private List<List<GNode<T>>> cityPaths = new ArrayList<>();

  RoadVisitor(Consumer<T> finishedRoadVisitor, Predicate<T> isCity) {
    this.isCity = Objects.requireNonNull(isCity);
    this.finishedRoadVisitor = Objects.requireNonNull(finishedRoadVisitor);
  }

  public void visitRoadsFrom(GNode<T> curr) {
    visit(List.of(), curr);
    if (isCity(curr) || cityPaths.size() > 1) {
      logger.info("found city {} paths", cityPaths.size());
      cityPaths.stream().flatMap(Collection::stream).forEach(this::markAsOnRoad);
    }
  }

  private void visit(List<GNode<T>> path, GNode<T> curr) {
    logger.debug("visiting: {} from {}", curr, path);
    if (visited.contains(curr)) {
      int idx = path.indexOf(curr);
      if (idx >= 0 && idx < path.size() - 1) {
        var loop = path.subList(idx, path.size());
        logger.info("found loop: {}", loop);
        loop.forEach(this::markAsOnRoad);
      }
      return;
    } else if (isCity(curr) && !path.isEmpty()) {
      cityPaths.add(listOf(path, curr));
      return;
    }
    visited.add(curr);
    GNode<T> prev = path.isEmpty() ? null : path.get(path.size() - 1);
    var newPath = listOf(path, curr);
    for (GNode<T> node : curr.nodes()) {
      if (!node.equals(prev)) {
        visit(newPath, node);
      }
    }
  }

  private List<GNode<T>> listOf(List<GNode<T>> path, GNode<T> curr) {
    if (path.isEmpty()) {
      return List.of(curr);
    }
    ArrayList<GNode<T>> newPath = new ArrayList<>(path.size() + 1);
    newPath.addAll(path);
    newPath.add(curr);
    return newPath;
  }

  private void markAsOnRoad(GNode<T> curr) {
    logger.info("marking road: {}", curr);
    if (!marked.contains(curr)) {
      marked.add(curr);
      finishedRoadVisitor.accept(curr.value());
    }
  }

  public boolean isCity(GNode<T> node) {
    return isCity.test(node.value());
  }
}
