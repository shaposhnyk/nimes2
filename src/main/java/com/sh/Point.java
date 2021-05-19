package com.sh;

import org.jetbrains.annotations.NotNull;

public record Point(int x, int y) {
  public Point plus(@NotNull Point p) {
    return new Point(p.x + x, p.y + y);
  }

  @Override
  public String toString() {
    return "p(" + x + "," + y + ')';
  }
}
