package com.sh.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RoadVisitor<T> {
  private static final Logger logger = LoggerFactory.getLogger(RoadVisitor.class);
  private final Consumer<T> finishedRoadVisitor;
  private final Predicate<T> isCity;
  private final Set<GNode<T>> visited = new HashSet<>();
  private final Set<GNode<T>> marked = new HashSet<>();
  private List<List<GNode<T>>> cityPaths = new ArrayList<>();

  RoadVisitor(Consumer<T> finishedRoadVisitor, Predicate<T> isCity) {
    this.finishedRoadVisitor = Objects.requireNonNull(finishedRoadVisitor);
    this.isCity = Objects.requireNonNull(isCity);
  }

  public void visitRoadsFrom(GNode<T> curr) {
    visit(List.of(), curr);
    if (isCity(curr) || cityPaths.size() > 1) {
      cityPaths.stream().flatMap(Collection::stream).forEach(this::markOnRoad);
    }
  }

  private void visit(List<GNode<T>> path, GNode<T> curr) {
    logger.info("visiting: {} from {}", curr, path);
    if (visited.contains(curr)) {
      int idx = path.indexOf(curr);
      if (idx >= 0 && idx < path.size() - 1) {
        path.subList(idx, path.size())
            .forEach(this::markOnRoad);
      }
      return;
    } else if (isCity(curr) && !path.isEmpty()) {
      cityPaths.add(listOf(path, curr));
      return;
    }
    visitNeighbors(path, curr);
  }

  private void visitNeighbors(List<GNode<T>> path, GNode<T> curr) {
    List<GNode<T>> newPath = listOf(path, curr);
    visited.add(curr);
    GNode<T> prev = path.isEmpty() ? null : path.get(path.size() - 1);
    for (GNode<T> node : curr.nodes()) {
      if (!node.equals(prev)) {
        visit(newPath, node);
      }
    }
  }

  private List<GNode<T>> listOf(List<GNode<T>> path, GNode<T> curr) {
    ArrayList<GNode<T>> newPath = new ArrayList<>(path.size() + 1);
    newPath.addAll(path);
    newPath.add(curr);
    return newPath;
  }

  private void markOnRoad(GNode<T> curr) {
    logger.info("finishing road: {}", curr);
    if (!marked.contains(curr)) {
      marked.add(curr);
      finishedRoadVisitor.accept(curr.value());
    }
  }

  public boolean isCity(GNode<T> node) {
    return isCity.test(node.value());
  }
}
