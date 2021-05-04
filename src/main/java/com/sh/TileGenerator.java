package com.sh;

import org.jetbrains.annotations.NotNull;

public interface TileGenerator {
  @NotNull TileNode generate();

  @NotNull TileNode rotate(@NotNull TileNode tileNode, int times);
}
