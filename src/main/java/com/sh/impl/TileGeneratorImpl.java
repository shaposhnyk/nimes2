package com.sh.impl;

import com.sh.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TileGeneratorImpl implements TileGenerator {
  private final Random rng;
  private final List<List<String>> list = List.of(
      List.of("-"),
      List.of("|"),
      List.of("/."),
      List.of("\\."),
      List.of("./"),
      List.of(".\\"),
      List.of("\\.", ".\\"),
      List.of("/.", "./")
  );
  private final int cityNum = 4;
  private final int crossNum = 7;
  private final List<Player> players;

  public TileGeneratorImpl(Random rng, List<Player> players) {
    this.rng = rng;
    this.players = players;
  }

  @Override
  public @NotNull TileNode generate() {
    if (rng.nextInt(crossNum) == 0) {
      return new TileNodeImpl(RoadSegment2D.cross());
    }
    List<RoadSegment> segs = list.get(rng.nextInt(list.size())).stream()
        .map(glyph -> RoadSegment2D.builderOf(glyph)
            .setSocket(rndSocket())
            .setType(rndType())
            .build()
        ).collect(Collectors.toUnmodifiableList());
    return new TileNodeImpl(segs);
  }

  private SegmentType rndType() {
    return rng.nextInt(cityNum) == 0 ? SimpleSegmentType.CITY : SimpleSegmentType.FIELD;
  }

  private Player rndSocket() {
    int idx = rng.nextInt(players.size() + 1);
    return idx == players.size() ? null : players.get(idx);
  }

  @Override
  public @NotNull TileNode rotate(@NotNull TileNode tileNode, int times) {
    if (times % Direction2D.values().length == 0) {
      return tileNode;
    }
    return new TileNodeImpl(
        tileNode.roads().stream()
            .map(rs -> rs.rotate(times))
            .collect(Collectors.toUnmodifiableList())
    );
  }
}
