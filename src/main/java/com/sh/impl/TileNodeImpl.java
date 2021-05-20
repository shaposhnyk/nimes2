package com.sh.impl;

import com.sh.RoadSegment;
import com.sh.TileNode;

import java.util.List;

public record TileNodeImpl(List<RoadSegment> roads) implements TileNode {
  TileNodeImpl(RoadSegment... roads) {
    this(List.of(roads));
  }
}
