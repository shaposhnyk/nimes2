package com.sh.impl;

import java.util.List;

public interface GraphRoad<T> {
  boolean isLoop();

  GraphRoad<T> tail();

  T first();

  T last();

  List<T> segments();
}
