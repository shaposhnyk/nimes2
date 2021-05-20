package com.sh.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RoadVisitorTest {

  @Test
  public void testDeadEnd() {
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c = nodeOf("Z");
    a.addMutually(b);
    b.addMutually(c);

    assertThat(visitRoad(a)).isEmpty();
    assertThat(visitRoad(b)).isEmpty();
    assertThat(visitRoad(c)).isEmpty();
  }

  @Test
  public void testY() {
    GNode<String> z0 = nodeOf("Z0");
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> y = nodeOf("Y");
    GNode<String> z = nodeOf("Z");
    z0.addMutually(a);
    y.addMutually(a);
    y.addMutually(b);
    y.addMutually(z);
    // Z0 - A \
    //      B - Y
    //      Z /

    assertThat(visitRoad(b)).isEmpty();
    assertThat(visitRoad(z)).containsOnly("Z0", "A", "Y", "Z");
    assertThat(visitRoad(z0)).containsOnly("Z0", "A", "Y", "Z");
  }

  @Test
  public void testY2() {
    GNode<String> z0 = nodeOf("Z0");
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> y = nodeOf("Y");
    GNode<String> z = nodeOf("Z");
    z0.addMutually(a);
    y.addMutually(a);
    y.addMutually(b);
    y.addMutually(z);
    // Z0 - A \
    //      B - Y
    //      Z /

    assertThat(visitRoad(a)).containsOnly("Z0", "A", "Y", "Z");
    assertThat(visitRoad(y)).containsOnly("Z0", "A", "Y", "Z");
  }

  @Test
  public void testSimpleRoad() {
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c1 = nodeOf("Z1");
    GNode<String> d = nodeOf("D");
    GNode<String> c2 = nodeOf("Z2");
    GNode<String> e = nodeOf("E");
    GNode<String> f = nodeOf("F");

    a.addMutually(b);
    b.addMutually(c1);
    c1.addMutually(d);
    d.addMutually(c2);
    c2.addMutually(e);
    e.addMutually(f);

    assertThat(visitRoad(a)).isEmpty();
    assertThat(visitRoad(b)).isEmpty();
    assertThat(visitRoad(c1)).contains("Z1", "D", "Z2").hasSize(3);
    assertThat(visitRoad(c2)).contains("Z1", "D", "Z2").hasSize(3);
    assertThat(visitRoad(e)).isEmpty();
    assertThat(visitRoad(f)).isEmpty();
  }

  @Test
  public void testSimpleRoad2() {
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c1 = nodeOf("Z1");
    GNode<String> d = nodeOf("D");
    GNode<String> c2 = nodeOf("Z2");
    GNode<String> e = nodeOf("E");
    GNode<String> f = nodeOf("F");

    a.addMutually(b);
    b.addMutually(c1);
    c1.addMutually(d);
    d.addMutually(c2);
    c2.addMutually(e);
    e.addMutually(f);

    assertThat(visitRoad(d)).contains("Z1", "D", "Z2").hasSize(3);
  }

  @Test
  public void testCircle() {
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c = nodeOf("C");
    GNode<String> d = nodeOf("D");

    a.addMutually(b);
    b.addMutually(c);
    c.addMutually(d);
    d.addMutually(a);

    assertThat(visitRoad(a)).containsOnly("A", "B", "C", "D");
    assertThat(visitRoad(b)).containsOnly("B", "C", "D", "A");
    assertThat(visitRoad(c)).containsOnly("C", "D", "A", "B");
    assertThat(visitRoad(d)).containsOnly("D", "A", "B", "C");
  }

  @Test
  public void testCircleWithCity() {
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c = nodeOf("C");
    GNode<String> d = nodeOf("Z");

    a.addMutually(b);
    b.addMutually(c);
    c.addMutually(d);
    d.addMutually(a);

    assertThat(visitRoad(a)).containsOnly("A", "B", "C", "D");
    assertThat(visitRoad(b)).containsOnly("B", "C", "D", "A");
    assertThat(visitRoad(c)).containsOnly("C", "D", "A", "B");
    assertThat(visitRoad(d)).containsOnly("D", "A", "B", "C");
  }

  @Test
  public void testSmallLoops() {
    GNode<String> z = nodeOf("Z");
    GNode<String> a = nodeOf("A");
    GNode<String> b = nodeOf("B");
    GNode<String> c = nodeOf("C");
    GNode<String> d = nodeOf("D");

    z.addMutually(a);
    a.addMutually(b);
    b.addMutually(z);

    z.addMutually(c);
    c.addMutually(d);
    d.addMutually(z);

    assertThat(visitRoad(a)).containsExactly("A", "B", "Z");
    assertThat(visitRoad(b)).containsExactly("A", "B", "Z");
    assertThat(visitRoad(z)).containsExactly("Z", "C", "D", "A", "B");
  }

  private List<String> visitRoad(GNode<String> root) {
    final List<String> list = new ArrayList<>();
    final RoadVisitor<String> visitor = new RoadVisitor<>(list::add, s -> s.startsWith("Z"));
    visitor.visitRoadsFrom(root);
    return list;
  }

  private GNode<String> nodeOf(String value) {
    return new GNode<>(value);
  }
}