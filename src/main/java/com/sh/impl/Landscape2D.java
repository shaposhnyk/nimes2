package com.sh.impl;

import com.sh.Direction;
import com.sh.Landscape;
import com.sh.Point;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Landscape2D<T> implements Landscape<T> {
  public enum Direction2D implements Direction {
    N, E, S, W
  }

  private static final Direction[] DIRS = new Direction2D[]{Direction2D.N, Direction2D.E, Direction2D.S, Direction2D.W};
  private static final Direction[] OPPOSITE_DIRS = new Direction2D[]{Direction2D.S, Direction2D.W, Direction2D.N, Direction2D.E};
  private static final Point[] DIR_POINTS = new Point[]{p(0, 1), p(1, 0), p(0, -1), p(-1, 0)};
  private static final Map<Direction, Point> DIR_MAP = Map.of(
      DIRS[0], DIR_POINTS[0],
      DIRS[1], DIR_POINTS[1],
      DIRS[2], DIR_POINTS[2],
      DIRS[3], DIR_POINTS[3]
  );
  private static final Map<Direction, Direction> OPPOSITE_MAP = Map.of(
      DIRS[0], OPPOSITE_DIRS[0],
      DIRS[1], OPPOSITE_DIRS[1],
      DIRS[2], OPPOSITE_DIRS[2],
      DIRS[3], OPPOSITE_DIRS[3]
  );

  private final Map<Point, T> map = new HashMap<>();
  private final Map<T, Point> elementPositions = new HashMap<>();
  private final Map<Point, Frontier<T>> frontierMap = new LinkedHashMap<>();

  public List<Frontier<T>> frontierSides() {
    List<Frontier<T>> sb = new ArrayList<>(frontierMap.size());
    Map.Entry<Point, Frontier<T>> startE = frontierMap.entrySet().iterator().next();
    Set<PointFrontier<T>> toVisit = frontierMap.entrySet().stream()
        .flatMap(f -> f.getValue().asUnitaryFrontiers().map(ff -> new PointFrontier<>(f.getKey(), ff)))
        .collect(Collectors.toSet());

    PointFrontier<T> curr = new PointFrontier<>(startE.getKey(), startE.getValue().withFirstSide());

    while (curr != null) {
      sb.add(curr.f);
      toVisit.remove(curr);
      curr = nextFrontierPoint(curr);
      if (!toVisit.contains(curr)) {
        curr = toVisit.isEmpty() ? null : toVisit.iterator().next();
      }
    }
    return sb;
  }

  private PointFrontier<T> nextFrontierPoint(PointFrontier<T> start) {
    Direction direction = start.f.firstSide();
    if (!(direction instanceof Landscape2D.Direction2D)) {
      throw new IllegalArgumentException("Unknown side: " + start.f.firstSide());
    }

    return switch ((Direction2D) direction) {
      case W -> pointOnFrontier(start.p, pf(-1, 1, Direction2D.S), pf(0, 1, Direction2D.W), pf(0, 0, Direction2D.N));
      case N -> pointOnFrontier(start.p, pf(1, 1, Direction2D.W), pf(1, 0, Direction2D.N), pf(0, 0, Direction2D.E));
      case E -> pointOnFrontier(start.p, pf(1, -1, Direction2D.N), pf(0, -1, Direction2D.E), pf(0, 0, Direction2D.S));
      case S -> pointOnFrontier(start.p, pf(-1, -1, Direction2D.E), pf(-1, 0, Direction2D.S), pf(0, 0, Direction2D.W));
    };
  }

  private PointFrontier<T> pointOnFrontier(Point p, PointFrontier<T>... candidates) {
    for (PointFrontier<T> candidate : candidates) {
      final Point cp = candidate.p.plus(p);
      final Frontier<T> frontier = frontierMap.get(cp);
      if (frontier != null && frontier.hasSide(candidate.f.firstSide())) {
        return new PointFrontier<>(cp, new Frontier<>(frontier.element(), candidate.f.directions()));
      }
    }
    throw new IllegalArgumentException("Frontier does not exist for: " + Arrays.toString(candidates));
  }

  private PointFrontier<T> pf(Point p, Direction s) {
    return new PointFrontier<>(p, new Frontier<>(null, s));
  }

  private PointFrontier<T> pf(int x, int y, Direction s) {
    return pf(p(x, y), s);
  }

  @Override
  public T get(Point p) {
    return map.get(p);
  }

  @Override
  public void add(Point p, T ch) {
    if (map.containsKey(p)) {
      throw new IllegalArgumentException();
    }

    if (frontierMap.isEmpty()) {
      frontierMap.put(p, new Frontier<>(ch, DIRS));
    } else {
      var newFront = new Frontier<>(ch, DIRS);
      for (int i = 0; i < DIR_POINTS.length; i++) {
        Point n = DIR_POINTS[i].plus(p);
        var frontier = frontierMap.get(n);
        if (frontier != null) {
          var newNeigh = frontier.withoutSide(OPPOSITE_DIRS[i]);
          if (newNeigh.isEmpty()) {
            frontierMap.remove(n);
          } else {
            frontierMap.put(n, newNeigh);
          }
          newFront = newFront.withoutSide(DIRS[i]);
        }
      }
      if (!newFront.isEmpty()) {
        frontierMap.put(p, newFront);
      }
    }

    map.put(p, ch);
    elementPositions.put(ch, p);
  }

  public void addInDirectionOf(T existingElement, Direction dir, T newElement) {
    Point existingPos = findOrThrow(existingElement);
    Point newElementPos = existingPos.plus(DIR_MAP.get(dir));
    if (map.containsKey(newElementPos)) {
      throw new IllegalArgumentException("Position " + newElementPos + "already occupied by: " + map.get(newElementPos));
    }
    add(newElementPos, newElement);
  }

  private Point findOrThrow(T element) {
    return Objects.requireNonNull(elementPositions.get(element), () -> "Unknown element: " + element);
  }

  @Override
  public @NotNull Map<Direction, Point> sides() {
    return Collections.unmodifiableMap(DIR_MAP);
  }

  @Override
  public Direction opposide(Direction dir) {
    return OPPOSITE_MAP.get(dir);
  }

  @Override
  public @NotNull List<Point> frontier() {
    return frontierSides().stream()
        .map(f -> elementPositions.get(f.element()).plus(DIR_MAP.get(f.firstSide())))
        .distinct()
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public @NotNull List<T> field() {
    return map.values().stream()
        .collect(Collectors.toUnmodifiableList());
  }

  public static Point p(int x, int y) {
    return new Point(x, y);
  }

  static record PointFrontier<T>(Point p, Frontier<T> f) {
  }
}
