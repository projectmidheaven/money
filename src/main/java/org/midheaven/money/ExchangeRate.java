package org.midheaven.money;

import org.midheaven.math.Rational;

public interface ExchangeRate extends MonetaryOperator<MonetaryAmount> {

    static ExchangeRate of(Currency source, Currency target, Rational rate) {
        return new RateExchangeRate(source, target, rate);
    }

    Currency source();
    Currency target();

    MonetaryAmount apply(MonetaryAmount amount);
}

final class RateExchangeRate extends AbstractExchangeRate {

    private final Rational rate;

    public RateExchangeRate(Currency source, Currency target, Rational rate) {
        super(source, target);
        this.rate = rate;
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount original) {
        MoneySupport.assertSameCurrency(original, source());
        return target().of(original.value().times(rate));
    }
}