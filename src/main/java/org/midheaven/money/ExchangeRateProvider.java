package org.midheaven.money;

public interface ExchangeRateProvider {

    ExchangeRate rateBetween(Currency source, Currency target);
}
