package org.midheaven.money;

import org.midheaven.collections.DistinctAssortment;
import org.midheaven.collections.Enumerable;
import org.midheaven.math.AdditionGroup;
import org.midheaven.math.Arithmetic;
import org.midheaven.math.Rational;

public interface MultiCurrencyMonetaryAmount extends AdditionGroup<MultiCurrencyMonetaryAmount> , Enumerable<MonetaryAmount> {

    static MultiCurrencyMonetaryAmount zero(){
        return new AssociationMultiCurrencyMoneyAmount();
    }

    static Arithmetic<MultiCurrencyMonetaryAmount, MultiCurrencyMonetaryAmount> arithmetic() {
        return Arithmetic.of(zero(), MultiCurrencyMonetaryAmount::plus , MultiCurrencyMonetaryAmount::over);
    }

    DistinctAssortment<Currency> currencies();

    MultiCurrencyMonetaryAmount plus(MonetaryAmount monetaryAmount);
    MultiCurrencyMonetaryAmount minus(MonetaryAmount monetaryAmount);

    MultiCurrencyMonetaryAmount times(long value);

    MultiCurrencyMonetaryAmount times(Rational value);

    default MultiCurrencyMonetaryAmount over(long value){
        return over(Rational.of(value));
    }

    default MultiCurrencyMonetaryAmount over(Rational value){
        return times(value.invert());
    }

    MonetaryAmount getAmount(Currency currency);

    MonetaryAmount reduceTo(Currency target, ExchangeRateProvider provider);
}
