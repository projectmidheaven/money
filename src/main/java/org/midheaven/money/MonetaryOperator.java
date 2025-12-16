package org.midheaven.money;

public interface MonetaryOperator<T> {

    T apply(MonetaryAmount amount);
}
