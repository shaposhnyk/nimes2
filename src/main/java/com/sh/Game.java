package com.sh;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Game {
  void startGame();

  @NotNull TileNode currentTile();

  @NotNull TileNode rotateCurrentTile();

  @NotNull List<Point> availableMoves();

  void putCurrentTileAt(@NotNull Point point);

  @NotNull PlayerStats statsFor(@NotNull Player player);
}
