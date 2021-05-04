package com.sh;

/**
 * Segment of the road on a tyle
 */
public interface RoadSegment {
  SegmentType type();

  SegmentColor color();

  Direction sideA();

  Direction sideB();

  boolean isPassBy(Direction side);
}
