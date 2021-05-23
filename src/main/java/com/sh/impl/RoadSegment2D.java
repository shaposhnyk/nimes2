package com.sh.impl;

import com.sh.Direction;
import com.sh.Player;
import com.sh.RoadSegment;
import com.sh.SegmentType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sh.impl.Direction2D.*;

public record RoadSegment2D(
    SegmentType type,
    Player socket,
    Set<Direction> connectedSides
) implements RoadSegment {

  private static final Map<String, Set<Direction>> map = Map.of(
      "|.", Set.of(N, S),
      "-.", Set.of(W, E),
      "/.", Set.of(W, N),
      "./", Set.of(S, E),
      "\\.", Set.of(W, S),
      ".\\", Set.of(N, E),
      "+.", Set.of(N, E, S, W)
  );

  public static RoadSegment2DBuilder builderOf(String glyph) {
    Set<Direction> dirs = Objects.requireNonNull(map.get(glyph.length() == 1 ? glyph + "." : glyph), () -> "Unknown glyph: '" + glyph + "'");
    return new RoadSegment2DBuilder().setConnectedSides(dirs);
  }

  public static RoadSegment2DBuilder builderOf(Direction... dirs) {
    return new RoadSegment2DBuilder().setConnectedSides(Set.copyOf(Arrays.asList(dirs)));
  }

  private static final Map<Set<Direction>, String> invMap = map.entrySet().stream()
      .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

  @Override
  public boolean isPassBy(Direction side) {
    return connectedSides.contains(side);
  }

  @Override
  public @NotNull RoadSegment rotate(int times) {
    return new RoadSegment2D(
        type,
        socket,
        connectedSides.stream()
            .map(s -> s.rotate(times))
            .collect(Collectors.toUnmodifiableSet())
    );
  }

  @Override
  public String toString() {
    String sides = invMap.containsKey(connectedSides) ? invMap.get(connectedSides) : connectedSides.toString();
    return SimpleSegmentType.CITY.equals(type) ? "[" + sides + "]" : "{" + sides + "}";
  }

  public static RoadSegment cross() {
    return builderOf("+")
        .setType(SimpleSegmentType.CITY)
        .build();
  }
}
