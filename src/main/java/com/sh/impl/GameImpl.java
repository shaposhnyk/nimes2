package com.sh.impl;

import com.sh.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GameImpl implements Game {
  private final TileGenerator generator;
  private final GameEngine engine;
  private TileNode currentTile;

  public GameImpl(TileGenerator generator, GameEngine engine) {
    this.generator = generator;
    this.engine = engine;
  }

  @Override
  public synchronized void startGame() {
    if (currentTile != null) {
      throw new IllegalStateException("Game already started");
    }
    getAndGenerateNext();
    putCurrentTileAt(new Point(0, 0));
  }

  @Override
  public synchronized @NotNull TileNode currentTile() {
    return currentTile;
  }

  @Override
  public synchronized @NotNull TileNode rotateCurrentTile() {
    this.currentTile = generator.rotate(currentTile, 1);
    return currentTile;
  }

  private synchronized @NotNull TileNode getAndGenerateNext() {
    TileNode curr = this.currentTile;
    this.currentTile = generator.generate();
    return curr;
  }

  @Override
  public @NotNull List<Point> availableMoves() {
    return engine.availableMoves();
  }

  @Override
  public void putCurrentTileAt(@NotNull Point point) {
    Objects.requireNonNull(currentTile, "Game should be started first");
    engine.putTile(point, getAndGenerateNext());
  }

  @Override
  public @NotNull PlayerStats statsFor(@NotNull Player player) {
    var stats = engine.stats().get(player);
    return Objects.requireNonNull(stats, () -> "Unknown player: " + player);
  }
}
