package com.sh.impl;

import com.sh.Direction;

enum Direction2D implements Direction {
  N, E, S, W;

  @Override
  public Direction rotate(int times) {
    return values()[(this.ordinal() + times) % values().length];
  }
}
