package com.sh.impl;

import java.util.HashSet;
import java.util.Set;

public class GNode<T> {
  private final T value;
  private final Set<GNode<T>> nodes = new HashSet<>(2);

  public GNode(T value) {
    this.value = value;
  }

  public T value() {
    return value;
  }

  public Set<GNode<T>> nodes() {
    return nodes;
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
