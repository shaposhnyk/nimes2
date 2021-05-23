package com.sh.impl;

import com.sh.Direction;
import com.sh.RoadSegment;
import com.sh.TileNode;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class TileGeneratorImplTest {
  private static final ColorPlayer RED = new ColorPlayer("RED");
  private static final ColorPlayer BLUE = new ColorPlayer("BLUE");
  final TileGeneratorImpl gen = new TileGeneratorImpl(new Random(1L), List.of(RED, BLUE));

  @Test
  public void testGenerate() {
    assertThat(gen.generate()).isNotNull();
  }

  @Test
  public void testRotateCross() {
    TileNodeImpl cross = new TileNodeImpl(RoadSegment2D.cross());

    assertThat(gen.rotate(cross, 1).roads()).hasSize(1)
        .extracting(RoadSegment::type)
        .containsOnly(SimpleSegmentType.CITY);

    assertThat(gen.rotate(cross, 1)).isEqualTo(cross);
    assertThat(gen.rotate(cross, 2)).isEqualTo(cross);
    assertThat(gen.rotate(cross, 3)).isEqualTo(cross);
    assertThat(gen.rotate(cross, 4)).isEqualTo(cross);
  }

  @Test
  public void testRotateNS() {
    TileNodeImpl ns = new TileNodeImpl(RoadSegment2D.builderOf("|").setSocket(RED).build());
    TileNodeImpl ew = new TileNodeImpl(RoadSegment2D.builderOf("-").setSocket(RED).build());

    assertThat(gen.rotate(ns, 1)).isEqualTo(ew);
    assertThat(gen.rotate(ns, 2)).isEqualTo(ns);
    assertThat(gen.rotate(ns, 3)).isEqualTo(ew);
    assertThat(gen.rotate(ns, 4)).isEqualTo(ns);
  }

  @Test
  public void testRotateWN() {
    TileNodeImpl ns = new TileNodeImpl(RoadSegment2D.builderOf(Direction2D.W, Direction2D.N).setSocket(BLUE).build());

    assertThat(gen.rotate(ns, 1)).isEqualTo(blue(Direction2D.N, Direction2D.E));
    assertThat(gen.rotate(ns, 2)).isEqualTo(blue(Direction2D.S, Direction2D.E));
    assertThat(gen.rotate(ns, 3)).isEqualTo(blue(Direction2D.S, Direction2D.W));
  }

  private TileNode blue(Direction... sides) {
    return new TileNodeImpl(RoadSegment2D.builderOf(sides).setSocket(BLUE).build());
  }
}