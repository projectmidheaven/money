package org.midheaven.money;

import org.midheaven.collections.AbstractEnumerableDecorator;
import org.midheaven.collections.Enumerable;
import org.midheaven.math.AdditionMonoid;

public class MonetaryAmountEnumerable extends AbstractEnumerableDecorator<MonetaryAmount> {


    private final Currency currency;

    public MonetaryAmountEnumerable(Enumerable<MonetaryAmount> original, Currency currency) {
        super(original.filter(it -> it.currency().equals(currency)));
        this.currency = currency;
    }

    public MonetaryAmount sum() {
        return reduce(AdditionMonoid::plus).orElse(currency.zero());
    }

    public MonetaryAmount average() {
        return reduce(AdditionMonoid::plus).map(total -> total.over(this.count().toInt())).orElse(currency.zero());
    }
}

class Increment {
    MonetaryAmount value;
    int count;
}