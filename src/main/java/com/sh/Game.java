package com.sh;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Game {
  @NotNull TileNode generate();

  @NotNull TileNode rotate(@NotNull TileNode tileNode, int times);

  @NotNull List<TileNode> field();

  @NotNull List<Point> frontier();

  void putTile(@NotNull Point p, @NotNull TileNode newTile);
}
