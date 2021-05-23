package com.sh.impl;

import com.sh.Direction;
import com.sh.Player;
import com.sh.SegmentType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RoadSegment2DBuilder {
  private SegmentType type = SimpleSegmentType.FIELD;
  private Player socket;
  private Set<Direction> connectedSides = new HashSet<>();

  public RoadSegment2DBuilder setType(SegmentType type) {
    this.type = type;
    return this;
  }

  public RoadSegment2DBuilder setSocket(Player socket) {
    this.socket = socket;
    return this;
  }

  public RoadSegment2DBuilder setConnectedSides(Set<Direction> connectedSides) {
    this.connectedSides = connectedSides;
    return this;
  }

  public RoadSegment2D build() {
    Objects.requireNonNull(type);
    return new RoadSegment2D(type, socket, Set.copyOf(connectedSides));
  }
}