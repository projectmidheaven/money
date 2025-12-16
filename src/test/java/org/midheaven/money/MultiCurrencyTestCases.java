package org.midheaven.money;

import org.junit.jupiter.api.Test;
import org.midheaven.math.Rational;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiCurrencyTestCases {

    @Test
    public void multiCurrencySum(){
        var value = MultiCurrencyMonetaryAmount.zero();
        var a = value.plus(Currency.parse("EUR").of(100))
                     .plus(Currency.parse("USD").of(100));

        var b = value.plus(Currency.parse("EUR").of(80))
                .plus(Currency.parse("JPY").of(90));

        var c = a.plus(b);

        var expected = value.plus(Currency.parse("EUR").of(180))
                .plus(Currency.parse("JPY").of(90))
                .plus(Currency.parse("USD").of(100));


        assertEquals(expected, c);

        assertEquals(Currency.parse("JPY").of(90), c.getAmount(Currency.parse("JPY")));
    }

    @Test
    public void multiCurrencySubtract(){
        var value = MultiCurrencyMonetaryAmount.zero();
        var a = value.plus(Currency.parse("EUR").of(100))
                .plus(Currency.parse("USD").of(100));

        var b = value.plus(Currency.parse("EUR").of(80))
                .plus(Currency.parse("JPY").of(90));

        var c = a.minus(b);

        var expected = value.plus(Currency.parse("EUR").of(20))
                .plus(Currency.parse("JPY").of(-90))
                .plus(Currency.parse("USD").of(100));


        assertEquals(expected, c);

        assertEquals(Currency.parse("JPY").of(-90), c.getAmount(Currency.parse("JPY")));
    }

    @Test
    public void multiCurrencyScale(){
        var EUR = Currency.parse("EUR");
        var JPY = Currency.parse("JPY");
        var USD = Currency.parse("USD");
        
        var value = MultiCurrencyMonetaryAmount.zero();
        var a = value.plus(EUR.of(100))
                .plus(USD.of(100))
                .plus(JPY.of(90));
        
        var c = a.times(2);

        var expected = value.plus(EUR.of(200))
                .plus(JPY.of(180))
                .plus(USD.of(200));


        assertEquals(expected, c);

        assertEquals(JPY.of(180), c.getAmount(JPY));
    }

    @Test
    public void reduction(){
        var value = MultiCurrencyMonetaryAmount.zero();
        var a = value.plus(Currency.parse("EUR").of(100))
                .plus(Currency.parse("USD").of(100))
                .plus(Currency.parse("JPY").of(90));

        var total = a.reduceTo(Currency.parse("BRL"), new ExchangeRateProvider() {
            @Override
            public ExchangeRate rateBetween(Currency source, Currency target) {
                return ExchangeRate.of(source, target, Rational.of(2));
            }

        });


        assertEquals(Currency.parse("BRL").of(580), total);
    }

}
