package org.midheaven.money;

import org.junit.jupiter.api.Test;
import org.midheaven.collections.Enumerable;
import org.midheaven.math.Rational;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyArithmeticTestCases {

    Currency EURO = Currency.parse("EUR");

    @Test
    public void sumMoneySequence(){
        var moneySequence = Enumerable.iterate(1L, i -> i + 1)
                .limit(1_000)
                .map(i -> EURO.of(i))
                .with(MonetaryAmount.arithmeticOf(EURO));

        assertEquals( EURO.of(500500),  moneySequence.sum());
        assertEquals( EURO.of(Rational.of(1001, 2)),  moneySequence.average());
    }

}
