package com.sh.impl;

public interface GNodeVisitor<T, R> {
  R visit(GNode<T> node);
}
