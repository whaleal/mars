package com.whaleal.mars.core.aggregation.expressions.impls;

public interface FieldHolder<T> {
    T field(String name, Expression expression);
}
