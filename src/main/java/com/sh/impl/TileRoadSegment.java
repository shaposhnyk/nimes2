package com.sh.impl;

import com.sh.RoadSegment;
import com.sh.TileNode;

public record TileRoadSegment(RoadSegment segment, TileNode tile) {
  @Override
  public String toString() {
    return segment.toString();
  }
}
