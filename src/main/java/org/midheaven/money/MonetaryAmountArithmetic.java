package org.midheaven.money;

import org.midheaven.math.Arithmetic;

public class MonetaryAmountArithmetic implements Arithmetic<MonetaryAmount, MonetaryAmount> {
    
    private final Currency currency;
    
    MonetaryAmountArithmetic(Currency currency){
        this.currency = currency;
    }
    
    @Override
    public MonetaryAmount zero() {
        return currency.zero();
    }
    
    @Override
    public MonetaryAmount sum(MonetaryAmount a, MonetaryAmount b) {
        if (a.isZero()){
            return b;
        } else if (b.isZero()){
            return a;
        }
        return a.plus(b);
    }
    
    @Override
    public MonetaryAmount over(MonetaryAmount monetaryAmount, long count) {
        return monetaryAmount.over(count);
    }
}
