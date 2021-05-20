package com.sh.impl;

import com.sh.Direction;
import com.sh.Player;
import com.sh.SegmentType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RoadSegmentImplBuilder {
  private SegmentType type = SimpleSegmentType.FIELD;
  private Player socket;
  private Set<Direction> connectedSides = new HashSet<>();

  public RoadSegmentImplBuilder setType(SegmentType type) {
    this.type = type;
    return this;
  }

  public RoadSegmentImplBuilder setSocket(Player socket) {
    this.socket = socket;
    return this;
  }

  public RoadSegmentImplBuilder setConnectedSides(Set<Direction> connectedSides) {
    this.connectedSides = connectedSides;
    return this;
  }

  public RoadSegmentImpl build() {
    Objects.requireNonNull(type);
    return new RoadSegmentImpl(type, socket, Set.copyOf(connectedSides));
  }
}