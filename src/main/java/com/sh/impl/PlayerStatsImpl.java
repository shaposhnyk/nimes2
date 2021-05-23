package com.sh.impl;

import com.sh.PlayerStats;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerStatsImpl implements PlayerStats {
  private final AtomicInteger claimed = new AtomicInteger(0);

  public int count() {
    return claimed.incrementAndGet();
  }

  @Override
  public int totalClaimed() {
    return claimed.intValue();
  }
}
