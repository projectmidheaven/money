package org.midheaven.money;

import org.junit.jupiter.api.Test;
import org.midheaven.collections.EditableSequence;
import org.midheaven.collections.ResizableSequence;
import org.midheaven.math.Rational;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MoneyDistributionTestCases {

    @Test
    public void fastWholeDistribution(){
        var value = Currency.parse("EUR").of(99);

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(33), sequence.getAt(0).orElse(null));
        assertFalse( sequence instanceof ResizableSequence<MonetaryAmount>);
        assertFalse( sequence instanceof EditableSequence<MonetaryAmount>);
    }

    @Test
    public void fastRemainingDistribution(){
        var value = Currency.parse("EUR").of(100);

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.34")), sequence.getAt(0).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.33")), sequence.getAt(1).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.33")), sequence.getAt(2).orElse(null));

    }
    @Test
    public void rationalWholeDistribution(){
        var value = new RationalMonetaryAmount(Currency.parse("EUR"), Rational.of(99));

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(33), sequence.getAt(0).orElse(null));
        assertFalse( sequence instanceof EditableSequence<MonetaryAmount>);
        assertFalse( sequence instanceof ResizableSequence<MonetaryAmount>);
    }

    @Test
    public void rationalRemainingDistribution(){
        var value = new RationalMonetaryAmount(Currency.parse("EUR"), Rational.of(100));

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.34")), sequence.getAt(0).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.33")), sequence.getAt(1).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.parse("33.33")), sequence.getAt(2).orElse(null));

    }

    @Test
    public void rationalFractionalDistribution(){
        var value = new RationalMonetaryAmount(Currency.parse("EUR"), Rational.of(1, 3));

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(0).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(1).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(2).orElse(null));

    }

    @Test
    public void rationalFractionalRemainderDistribution(){
        var value = new RationalMonetaryAmount(Currency.parse("EUR"), Rational.parse("0.336"));

        var sequence = value.distribute(3);

        assertEquals(3, sequence.count().toInt());
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(0).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(1).orElse(null));
        assertEquals(Currency.parse("EUR").of(Rational.of(11, 100)), sequence.getAt(2).orElse(null));

    }

}
