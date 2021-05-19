package com.sh.impl;

import com.sh.Landscape;
import org.junit.Test;

import static com.sh.impl.Landscape2D.p;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Landscape2DTest {
  final Landscape<Character> ls = new Landscape2D<>();

  @Test
  public void onePoint() {
    ls.add(p(0, 0), 'a');
    assertThat(ls.elements()).containsExactly('a');
    assertThat(ls.frontier())
        .containsOnly(p(0, 1), p(1, 0), p(0, -1), p(-1, 0));
  }

  @Test
  public void twoPointsV() {
    ls.add(p(0, 0), 'a');
    ls.add(p(0, 1), 'b');
    assertThat(ls.elements()).containsOnly('a', 'b');
    assertThat(ls.frontier())
        .containsOnly(p(0, -1), p(0, 2), p(1, 0), p(-1, 0), p(1, 1), p(-1, 1));
  }

  @Test
  public void threePointsV() {
    ls.add(p(0, 0), 'a');
    ls.add(p(0, 1), 'b');
    ls.add(p(0, 2), 'c');
    assertThat(ls.elements()).containsOnly('a', 'b', 'c');
    assertThat(ls.frontier())
        .containsOnly(p(0, -1), p(0, 3), p(1, 0), p(-1, 0), p(1, 1), p(-1, 1), p(1, 2), p(-1, 2));
  }

  @Test
  public void threePointsL() {
    ls.add(p(0, 0), 'a');
    ls.add(p(1, 0), 'b');
    ls.add(p(1, 1), 'c');
    assertThat(ls.elements()).containsOnly('a', 'b', 'c');
    assertThat(ls.frontier())
        .containsOnly(p(-1, 0), p(0, -1), p(1, -1), p(2, 0), p(2, 1), p(1, 2), p(0, 1));
  }

  @Test
  public void fourPointsO() {
    ls.add(p(0, 0), 'a');
    ls.add(p(1, 0), 'b');
    ls.add(p(1, 1), 'c');
    ls.add(p(0, 1), 'd');
    assertThat(ls.elements()).containsOnly('a', 'b', 'c', 'd');
    assertThat(ls.frontier())
        .containsOnly(p(-1, 0), p(0, -1), p(1, -1), p(2, 0), p(2, 1), p(1, 2), p(0, 2), p(-1, 1));
  }

  @Test
  public void eightPointsO() {
    ls.add(p(-1, 1), 'a');
    ls.add(p(0, 1), 'b');
    ls.add(p(1, 1), 'c');
    ls.add(p(1, 0), 'd');//-
    ls.add(p(-1, -1), 'e');
    ls.add(p(0, -1), 'f');
    ls.add(p(1, -1), 'g');
    ls.add(p(-1, 0), 'h');//-
    assertThat(ls.elements()).hasSize(8);
    assertThat(ls.frontier())
        .contains(p(-1, 2), p(0, 2), p(1, 2))
        .contains(p(-1, -2), p(0, -2), p(1, -2))
        .contains(p(-2, -1), p(-2, 0), p(-2, 1))
        .contains(p(2, -1), p(2, 0), p(2, 1))
        .contains(p(0, 0))
        .hasSize(3 * 4 + 1);
  }

  @Test
  public void ninePointsO() {
    ls.add(p(-1, 1), 'a');
    ls.add(p(0, 1), 'b');
    ls.add(p(1, 1), 'c');
    ls.add(p(1, 0), 'd');//-
    ls.add(p(-1, -1), 'e');
    ls.add(p(0, -1), 'f');
    ls.add(p(1, -1), 'g');
    ls.add(p(-1, 0), 'h');//-
    ls.add(p(0, 0), 'X');
    assertThat(ls.elements()).hasSize(9);
    assertThat(ls.frontier())
        .contains(p(-1, 2), p(0, 2), p(1, 2))
        .contains(p(-1, -2), p(0, -2), p(1, -2))
        .contains(p(-2, -1), p(-2, 0), p(-2, 1))
        .contains(p(2, -1), p(2, 0), p(2, 1))
        .hasSize(3 * 4);
  }

  @Test
  public void twoPointsH() {
    ls.add(p(0, 0), 'a');
    ls.add(p(1, 0), 'b');
    assertThat(ls.elements()).containsOnly('a', 'b');
    assertThat(ls.frontier())
        .containsOnly(p(-1, 0), p(2, 0), p(0, 1), p(0, -1), p(1, 1), p(1, -1));
  }

  @Test
  public void twoPointsVM() {
    ls.add(p(0, 0), 'a');
    ls.add(p(0, -1), 'b');
    assertThat(ls.elements()).containsOnly('a', 'b');
    assertThat(ls.frontier())
        .containsOnly(p(0, -2), p(0, 1), p(1, 0), p(-1, 0), p(1, -1), p(-1, -1));
  }

  @Test
  public void samePoint() {
    ls.add(p(0, 0), 'a');
    assertThatThrownBy(() -> ls.add(p(0, 0), 'b')).hasMessageContaining("occupied");
  }

  @Test
  public void twoPointsDistinct() {
    ls.add(p(0, 0), 'a');
    ls.add(p(0, 10), 'b');
    assertThat(ls.elements()).containsOnly('a', 'b');
    assertThat(ls.frontier())
        .contains(p(0, 1), p(1, 0), p(0, -1), p(-1, 0))
        .contains(p(0, 11), p(1, 10), p(0, 9), p(-1, 10))
        .hasSize(8);
  }
}