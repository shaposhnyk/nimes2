package com.sh;

import java.util.List;

/**
 * Abstract TileNode contains list of road segments
 */
public interface TileNode {
  List<RoadSegment> roads();
}
