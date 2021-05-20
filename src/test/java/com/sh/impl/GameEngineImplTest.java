package com.sh.impl;

import com.sh.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static com.sh.impl.Landscape2D.p;
import static org.assertj.core.api.Assertions.assertThat;

public class GameEngineImplTest extends TestCase {
  final Player R = new ColorPlayer("RED");
  final Player B = new ColorPlayer("BLUE");
  final TileGenerator generator = null;
  final Landscape<List<GNode<TileRoadSegment>>> landscape = new Landscape2D<>();
  final GameEngine ge = new GameEngineImpl(generator, landscape, List.of(R, B));

  @Test
  public void testEngine() {
    putTiles(1, tile(B, "/"), tile(B, "l"));
    putTiles(0, tile(R, "L"));

    assertThat(ge.stats().values())
        .extracting(PlayerStats::totalClaimed)
        .containsOnly(0);

    ge.putTile(p(1, 0), tile("+"));
    assertThat(ge.stats().get(B))
        .extracting(PlayerStats::totalClaimed)
        .isEqualTo(2);

    assertThat(ge.stats().get(R))
        .extracting(PlayerStats::totalClaimed)
        .isEqualTo(1);

  }

  private void putTiles(int y, TileNode... tiles) {
    for (int i = 0; i < tiles.length; i++) {
      ge.putTile(p(i, y), tiles[i]);
    }
  }

  private TileNode tile(Player p, String s) {
    return switch (s) {
      case "|" -> new TileNodeImpl(RoadSegmentImpl.northSouth().setSocket(p).build());
      case "-" -> new TileNodeImpl(RoadSegmentImpl.westEast().setSocket(p).build());
      case "/" -> new TileNodeImpl(RoadSegmentImpl.westNorth().setSocket(p).build());
      case "l" -> new TileNodeImpl(RoadSegmentImpl.eastSouth().setSocket(p).build());
      case "L" -> new TileNodeImpl(RoadSegmentImpl.northEast().setSocket(p).build());
      default -> throw new IllegalArgumentException("unknown type: " + s);
    };
  }

  private TileNode tile(String s) {
    return switch (s) {
      case "+" -> new TileNodeImpl(RoadSegmentImpl.cross());
      case "|" -> new TileNodeImpl(RoadSegmentImpl.northSouth().build());
      case "-" -> new TileNodeImpl(RoadSegmentImpl.westEast().build());
      default -> throw new IllegalArgumentException("unknown type: " + s);
    };
  }
}