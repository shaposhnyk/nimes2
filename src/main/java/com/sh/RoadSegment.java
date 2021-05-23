package com.sh;

import org.jetbrains.annotations.NotNull;

/**
 * Segment of the road on a tile
 */
public interface RoadSegment {
  /**
   * @return segment type, i.e. City, Field, etc
   */
  @NotNull SegmentType type();

  /**
   * @return player ID or null if there is no player's place
   */
  Player socket();

  /**
   * @return true if this road segment passes by a given tile side
   */
  boolean isPassBy(Direction side);

  @NotNull RoadSegment rotate(int times);
}
