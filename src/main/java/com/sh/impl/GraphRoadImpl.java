package com.sh.impl;

import com.sh.RoadSegment;

import java.util.List;

public class GraphRoadImpl<T> implements GraphRoad<T> {
  public static GraphRoad<RoadSegment> empty() {
    return new GraphRoad<>() {
      @Override
      public boolean isLoop() {
        return false;
      }

      @Override
      public GraphRoad<RoadSegment> tail() {
        throw new IllegalStateException();
      }

      @Override
      public RoadSegment first() {
        throw new IllegalStateException();
      }

      @Override
      public RoadSegment last() {
        throw new IllegalStateException();
      }

      @Override
      public List<RoadSegment> segments() {
        return List.of();
      }
    };
  }

  @Override
  public boolean isLoop() {
    return false;
  }

  @Override
  public GraphRoad<T> tail() {
    return null;
  }

  @Override
  public T first() {
    return null;
  }

  @Override
  public T last() {
    return null;
  }

  @Override
  public List<T> segments() {
    return null;
  }
}
