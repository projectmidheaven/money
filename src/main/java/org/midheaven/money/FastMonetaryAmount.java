package org.midheaven.money;

import org.midheaven.collections.EditableSequence;
import org.midheaven.collections.Sequence;
import org.midheaven.lang.HashCode;
import org.midheaven.lang.ValueClass;
import org.midheaven.math.Rational;

@ValueClass
final class FastMonetaryAmount implements MonetaryAmount {

    final long minorUnits;
    final Currency currency;

    FastMonetaryAmount(Currency currency, long minorUnits){
        this.minorUnits = minorUnits;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof FastMonetaryAmount fast){
           return this.minorUnits == fast.minorUnits
                && this.currency.equals(fast.currency);
        }
        return other instanceof MonetaryAmount monetaryAmount
                && this.value().equals(monetaryAmount.value())
                && this.currency.equals(monetaryAmount.currency());
    }

    @Override
    public int hashCode(){
        return HashCode.asymmetric().add(this.value()).add(this.currency).hashCode();
    }

    @Override
    public String toString(){
        return currency.code() + " " + value();
    }
    @Override
    public int compareTo(MonetaryAmount other) {
        if (other == null){
            throw new NullPointerException();
        }
        MoneySupport.assertSameCurrency(this, other);
        if (other instanceof FastMonetaryAmount fast){
            return Long.compare(this.minorUnits, fast.minorUnits);
        }
        return -other.compareTo(this);
    }

    @Override
    public MonetaryAmount negate() {
        return new FastMonetaryAmount(currency, -minorUnits);
    }

    @Override
    public MonetaryAmount plus(MonetaryAmount money) {
        MoneySupport.assertSameCurrency(this, money);
        if (money instanceof FastMonetaryAmount fastMoney){
            try {
                return new FastMonetaryAmount(currency, Math.addExact(this.minorUnits , fastMoney.minorUnits));
            } catch (ArithmeticException e){
                // fall back to promotion
            }
        }
        return new RationalMonetaryAmount(currency, this.value()).plus(money);
    }

    @Override
    public MonetaryAmount minus(MonetaryAmount money) {
        MoneySupport.assertSameCurrency(this, money);
        if (money instanceof FastMonetaryAmount fastMoney){
            try {
                return new FastMonetaryAmount(currency, Math.subtractExact(this.minorUnits , fastMoney.minorUnits));
            } catch (ArithmeticException e){
                // fall back to promotion
            }
        }
        return new RationalMonetaryAmount(currency, this.value()).minus(money);
    }

    @Override
    public MonetaryAmount abs() {
        if (minorUnits >= 0){
            return this;
        }
        return negate();
    }

    @Override
    public boolean isZero() {
        return minorUnits == 0;
    }

    @Override
    public Currency currency() {
        return currency;
    }

    @Override
    public Rational value() {
        return Rational.of(this.minorUnits).over(Rational.TEN.raisedTo(currency.exponent()));
    }

    @Override
    public Sequence<MonetaryAmount> distribute(int count) {

        var q = this.minorUnits / count;

        EditableSequence<MonetaryAmount> sequence = Sequence.builder().withSize(count).editable().repeat(new FastMonetaryAmount(currency, q));

        var cent = new FastMonetaryAmount(currency, 1);
        var r = this.minorUnits % count;
        var index = 0;
        while (r > 0) {
            sequence.setAt(index, sequence.getAt(index).orElse(this.currency.zero()).plus(cent));
            index++;
            r--;
        }
        return Sequence.builder().from(sequence);
    }

    @Override
    public MonetaryAmount times(long value) {
        try {
            return new FastMonetaryAmount(this.currency, Math.multiplyExact(this.minorUnits , value));
        } catch (ArithmeticException e){
            return new RationalMonetaryAmount(currency, this.value()).times(value);
        }
    }

    @Override
    public MonetaryAmount times(Rational value) {
        return new RationalMonetaryAmount(currency, this.value()).times(value);
    }

}
