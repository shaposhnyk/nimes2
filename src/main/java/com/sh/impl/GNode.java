package com.sh.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GNode<T> {
  final T value;
  final Set<GNode<T>> nodes = new HashSet<>(2);

  public GNode(T value) {
    this.value = value;
  }

  public T value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GNode<?> gNode = (GNode<?>) o;
    return value.equals(gNode.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "GNode{" +
        "value=" + value +
        '}';
  }

  public void add(GNode<T> another) {
    this.nodes.add(another);
  }

  public void addMutually(GNode<T> anotherNode) {
    this.add(anotherNode);
    anotherNode.add(this);
  }
}
