package com.sh;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface GameEngine {
  /**
   * @return list of tiles in game
   */
  @NotNull List<TileNode> field();

  /**
   * @return list of available moves
   */
  @NotNull List<Point> availableMoves();

  /**
   * Adds new tile in game
   */
  void putTile(@NotNull Point p, @NotNull TileNode newTile);

  /**
   * @return current statistics
   */
  Map<Player, PlayerStats> stats();
}
