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

  /**
   * @return new random tile
   */
  @NotNull TileNode generate();

  /**
   * @return new tile corresponding to the given one rotated n time clockwise
   */
  @NotNull TileNode rotate(@NotNull TileNode tileNode, int n);
}
