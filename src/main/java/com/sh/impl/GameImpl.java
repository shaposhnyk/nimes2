package com.sh.impl;

import com.sh.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class GameImpl implements Game {
  final TileGenerator generator;
  final Landscape<List<GNode<TileRoadSegment>>> landscape;
  final RoadVisitor<TileRoadSegment> roadVisitor = new RoadVisitor<>(null, null);

  public GameImpl(TileGenerator generator, Landscape<List<GNode<TileRoadSegment>>> landscape) {
    this.generator = generator;
    this.landscape = landscape;
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
    return null;
  }

  @Override
  public @NotNull List<Point> frontier() {
    return landscape.frontier();
  }

  @Override
  public void putTile(Point p, TileNode newTile) {
    var gNodes = createGraphNodes(newTile);
    landscape.add(p, gNodes);

    // connect new nodes
    for (Map.Entry<Direction, Point> dir : landscape.sides().entrySet()) {
      Point n = p.plus(dir.getValue());
      if (landscape.get(n) != null) {
        connectGraphNodes(p, dir.getKey(), n);
      }
    }

    for (GNode<TileRoadSegment> gnode : gNodes) {
      roadVisitor.visitRoadFrom(gnode);
    }
  }

  public void count(GraphRoad<RoadSegment> road) {

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
      if (node.value().segment().isPassBy(pSide)) {
        for (GNode<TileRoadSegment> neighborNode : landscape.get(n)) {
          Direction nSide = landscape.opposite(pSide);
          if (neighborNode.value().segment().isPassBy(nSide)) {
            node.addMutually(neighborNode);
          }
        }
      }
    }
  }
}
