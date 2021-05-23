package com.sh.impl;

import com.sh.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static com.sh.impl.Direction2D.*;
import static com.sh.impl.Landscape2D.p;
import static org.assertj.core.api.Assertions.assertThat;

public class GameEngineImplTest extends TestCase {
  static final Player R = new ColorPlayer("RED");
  static final Player B = new ColorPlayer("BLUE");
  final TileGenerator generator = null;
  final Landscape<List<GNode<TileRoadSegment>>> landscape = new Landscape2D<>();
  final GameEngineImpl ge = new GameEngineImpl(generator, landscape, List.of(R, B));

  @Test
  public void testLoopAdd() {
    addTiles(1, tile(B, S, E), tile(B, W, S));
    addTiles(0, tile(R, N, E));

    ge.putTile(p(1, 0), tile("+"));
    assertThat(ge.stats().get(B))
        .extracting(PlayerStats::totalClaimed)
        .isEqualTo(2);

    assertThat(ge.stats().get(R))
        .extracting(PlayerStats::totalClaimed)
        .isEqualTo(1);

  }

  @Test
  public void testLoopPut() {
    putTiles(1, tile(B, S, E), tile(B, W, S));
    putTiles(0, tile(R, N, E));

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

  private void addTiles(int y, TileNode... tiles) {
    for (int i = 0; i < tiles.length; i++) {
      ge.addTile(p(i, y), tiles[i]);
    }
  }

  private TileNode tile(Player p, Direction... sides) {
    return new TileNodeImpl(RoadSegmentImpl.builderOf(sides).setSocket(p).build());
  }

  private TileNode tile(Player p, String s) {
    return new TileNodeImpl(RoadSegmentImpl.builderOf(s).setSocket(p).build());
  }

  private TileNode tile(String s) {
    return new TileNodeImpl(RoadSegmentImpl.builderOf(s).setType(SimpleSegmentType.CITY).build());
  }
}