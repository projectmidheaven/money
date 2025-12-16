package org.midheaven.money;

import org.midheaven.lang.Maybe;
import org.midheaven.lang.Strings;
import org.midheaven.math.Rational;

public interface Currency {

    static Currency parse(String code){
       return Strings.filled(code)
               .map(java.util.Currency::getInstance)
               .map(it -> new IsoCurrency(CurrencyCode.parse(code), it.getDefaultFractionDigits()))
               .orNull();
    }
    
    static Currency of(CurrencyCode code){
        return Maybe.of(code)
                   .map(it -> java.util.Currency.getInstance(it.isoCode()))
                   .map(it -> new IsoCurrency(code, it.getDefaultFractionDigits()))
                   .orElse(null);
    }

    default MonetaryAmount zero(){
        return new FastMonetaryAmount(this, 0);
    }

    default MonetaryAmount of(long value){
        return new FastMonetaryAmount( this,Rational.TEN.raisedTo(this.exponent()).times(value).toLong());
    }

    default MonetaryAmount of(Rational value){
        if (value == null){
            return null;
        }
        var minorUnits = value.times(Rational.TEN.raisedTo(this.exponent()));
        if (minorUnits.isWhole()){
            return new FastMonetaryAmount( this, minorUnits.toLong());
        }
        return new RationalMonetaryAmount(this , value);
    }

    int exponent();
    CurrencyCode code();
}
