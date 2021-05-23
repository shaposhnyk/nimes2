package com.sh.impl;

import com.sh.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GameEngineImpl implements GameEngine {
  private static final Logger logger = LoggerFactory.getLogger(GameEngineImpl.class);
  private final TileGenerator generator;
  private final Landscape<List<GNode<TileRoadSegment>>> landscape;
  private final Map<Point, TileNode> tiles;
  private final Map<Player, PlayerStatsImpl> playerStats;

  public GameEngineImpl(
      TileGenerator generator,
      Landscape<List<GNode<TileRoadSegment>>> landscape,
      List<Player> players
  ) {
    this.generator = generator;
    this.landscape = landscape;
    this.tiles = new HashMap<>();
    this.playerStats = players.stream().collect(toMap(Function.identity(), p -> new PlayerStatsImpl()));
  }

  @Override
  public @NotNull TileNode generate() {
    return generator.generate();
  }

  @Override
  public @NotNull TileNode rotate(TileNode tileNode, int times) {
    return generator.rotate(tileNode, times);
  }

  @Override
  public @NotNull List<TileNode> field() {
    return tiles.values().stream()
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public @NotNull List<Point> availableMoves() {
    return landscape.frontier();
  }

  @Override
  public void putTile(Point p, TileNode newTile) {
    var newNodes = addTile(p, newTile);
    var roadVisitor = new RoadVisitor<>(this::closeRoadSegment, this::isCity);
    newNodes.forEach(roadVisitor::visitRoadsFrom);
  }

  public List<GNode<TileRoadSegment>> addTile(Point p, TileNode newTile) {
    logger.info("Adding tile at {}: {}", p, newTile);
    tiles.put(p, newTile);
    var newNodes = createGraphNodes(newTile);
    landscape.add(p, newNodes);

    // connect new nodes
    for (Map.Entry<Direction, Point> dir : landscape.sides().entrySet()) {
      Point n = p.plus(dir.getValue());
      if (landscape.get(n) != null) {
        connectGraphNodes(p, dir.getKey(), n);
      }
    }

    return newNodes;
  }

  @Override
  public Map<Player, PlayerStats> stats() {
    return playerStats.entrySet().stream()
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public void closeRoadSegment(TileRoadSegment closedSegment) {
    Player socket = closedSegment.segment().socket();
    if (socket != null) {
      playerStats.get(socket).count();
    }
  }

  private boolean isCity(TileRoadSegment tileSegment) {
    return isCity(tileSegment.segment());
  }

  private boolean isCity(RoadSegment segment) {
    return SimpleSegmentType.CITY.equals(segment.type());
  }

  private List<GNode<TileRoadSegment>> createGraphNodes(TileNode tile) {
    return tile.roads().stream()
        .map(r -> new TileRoadSegment(r, tile))
        .map(GNode::new)
        .collect(toList());
  }

  private void connectGraphNodes(Point p, Direction pSide, Point n) {
    for (GNode<TileRoadSegment> node : landscape.get(p)) {
      if (!node.value().segment().isPassBy(pSide)) {
        continue;
      }
      
      for (GNode<TileRoadSegment> neighborNode : landscape.get(n)) {
        Direction nSide = landscape.opposite(pSide);
        if (neighborNode.value().segment().isPassBy(nSide)) {
          node.addMutually(neighborNode);
        }
      }
    }
  }

  @Override
  public String toString() {
    return "GameEngineImpl{" +
        "landscape=" + landscape +
        '}';
  }
}
