package com.sh.impl;

import com.sh.Direction;
import com.sh.Player;
import com.sh.RoadSegment;
import com.sh.SegmentType;

import java.util.Set;

import static com.sh.impl.Direction2D.*;

public record RoadSegmentImpl(
    SegmentType type,
    Player socket,
    Set<Direction> connectedSides
) implements RoadSegment {

  @Override
  public boolean isPassBy(Direction side) {
    return connectedSides.contains(side);
  }

  public static RoadSegmentImplBuilder eastSouth() {
    return new RoadSegmentImplBuilder().setConnectedSides(Set.of(E, S));
  }

  public static RoadSegmentImplBuilder westEast() {
    return new RoadSegmentImplBuilder().setConnectedSides(Set.of(W, E));
  }

  public static RoadSegmentImplBuilder northSouth() {
    return new RoadSegmentImplBuilder().setConnectedSides(Set.of(N, S));
  }

  public static RoadSegmentImplBuilder westNorth() {
    return new RoadSegmentImplBuilder().setConnectedSides(Set.of(W, N));
  }

  public static RoadSegmentImplBuilder northEast() {
    return new RoadSegmentImplBuilder().setConnectedSides(Set.of(N, E));
  }

  public static RoadSegment cross() {
    return new RoadSegmentImplBuilder()
        .setType(SimpleSegmentType.CITY)
        .setConnectedSides(Set.of(E, W, N, S))
        .build();
  }
}
