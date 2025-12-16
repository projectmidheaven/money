package org.midheaven.money;

import org.midheaven.collections.Sequence;
import org.midheaven.lang.HashCode;
import org.midheaven.math.Rational;

/*value*/ public class RationalMonetaryAmount implements MonetaryAmount {

    private final Currency currency;
    private final Rational value;

    RationalMonetaryAmount(Currency currency, Rational value){
        this.currency = currency;
        this.value = value;
    }

    @Override
    public Currency currency() {
        return currency;
    }

    @Override
    public Rational value() {
        return value;
    }

    @Override
    public Sequence<MonetaryAmount> distribute(int count) {
        var power = Rational.TEN.raisedTo(currency.exponent());
        var canonical = this.value.times(power).toLong();

        return new FastMonetaryAmount(this.currency, canonical).distribute(count);
    }

    @Override
    public MonetaryAmount times(long value) {
        return new RationalMonetaryAmount(this.currency, this.value.times(value));
    }

    @Override
    public MonetaryAmount times(Rational value) {
        return new RationalMonetaryAmount(this.currency, this.value.times(value));
    }

    @Override
    public int compareTo(MonetaryAmount other) {
        if (other == null){
            throw new NullPointerException();
        }
        MoneySupport.assertSameCurrency(this, other);
        return this.value.compareTo(other.value());
    }

    @Override
    public MonetaryAmount negate() {
        return new RationalMonetaryAmount(this.currency , this.value.negate());
    }

    @Override
    public MonetaryAmount minus(MonetaryAmount money) {
        MoneySupport.assertSameCurrency(this, money);
        return new RationalMonetaryAmount(this.currency, this.value.minus(money.value()));
    }

    @Override
    public MonetaryAmount plus(MonetaryAmount money) {
        MoneySupport.assertSameCurrency(this, money);
        return new RationalMonetaryAmount(this.currency, this.value.plus(money.value()));
    }

    @Override
    public MonetaryAmount abs() {
        return new RationalMonetaryAmount(this.currency , this.value.abs());
    }

    @Override
    public boolean isZero() {
        return this.value.isZero();
    }

    @Override
    public boolean equals(Object other){
        return other instanceof MonetaryAmount monetaryAmount
                && this.value.equals(monetaryAmount.value())
                && this.currency.equals(monetaryAmount.currency());
    }


    @Override
    public int hashCode(){
        return HashCode.asymmetric().add(this.value).add(this.currency).hashCode();
    }

    @Override
    public String toString(){
        return currency.code() + " " + value();
    }
}
