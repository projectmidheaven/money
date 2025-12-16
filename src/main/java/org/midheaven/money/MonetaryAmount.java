package org.midheaven.money;

import org.midheaven.collections.Sequence;
import org.midheaven.lang.Check;
import org.midheaven.lang.NotNullable;
import org.midheaven.lang.Nullable;
import org.midheaven.lang.Ordered;
import org.midheaven.math.AdditionGroup;
import org.midheaven.math.Arithmetic;
import org.midheaven.math.Interval;
import org.midheaven.math.Rational;

public interface MonetaryAmount extends Ordered<MonetaryAmount>, AdditionGroup<MonetaryAmount> {
    
    static @NotNullable Arithmetic<MonetaryAmount, MonetaryAmount> arithmeticOf(@NotNullable  Currency currency){
        Check.argumentIsNotNull("currency", currency);
        return new MonetaryAmountArithmetic(currency);
    }
    
    static Interval.Domain<MonetaryAmount, MonetaryAmount> domain(){
        return new Interval.Domain<>() {
            @Override
            public int compare(MonetaryAmount a, MonetaryAmount b) {
                return a.compareTo(b);
            }
            
            @Override
            public MonetaryAmount applyMinimum(MonetaryAmount value) {
                return value;
            }
            
            @Override
            public MonetaryAmount applyMaximum(MonetaryAmount value) {
                return value;
            }
        };
    }
    
    static @Nullable MonetaryAmount zero(@Nullable Currency currency){
        if (currency == null){
            return null;
        }
        return currency.zero();
    }

    static @Nullable MonetaryAmount of(@Nullable Currency currency, long value){
        if (currency == null){
            return null;
        }
        return currency.of(value);
    }

    static @Nullable MonetaryAmount of(@Nullable Currency currency, @Nullable Rational value){
        if (currency == null || value == null){
            return null;
        }
        return currency.of(value);
    }



    Currency currency();
    Rational value();

    @NotNullable Sequence<MonetaryAmount> distribute(int count);

    default <R> R with(@NotNullable MonetaryOperator<R> operator){
        return operator.apply(this);
    }
    
    @NotNullable MonetaryAmount times(long value);
    
    @NotNullable MonetaryAmount times(@NotNullable Rational value);

    default MonetaryAmount over(long value){
        return over(Rational.of(value));
    }

    default MonetaryAmount over(Rational value){
        return times(value.invert());
    }
}
